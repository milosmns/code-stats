package github

import Printable
import PrintableLn
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.github.graphql.GetDiscussionCommentsPageQuery
import com.github.graphql.GetDiscussionQuery
import com.github.graphql.GetDiscussionsPageQuery
import com.github.graphql.type.DiscussionOrder
import com.github.graphql.type.DiscussionOrderField
import com.github.graphql.type.OrderDirection
import github.mapping.toGitHubDiscussion
import github.mapping.withRepliesToGitHubComments
import github.models.GitHubDiscussion
import models.TeamHistoryConfig
import parallelMap
import printable
import printableLn

class GitHubDiscussionFetcher(
  private val repository: String,
  private val teamHistoryConfig: TeamHistoryConfig,
  private val gitHubHistoryConfig: GitHubHistoryConfig,
  private val graphQlClient: ApolloClient,
) {

  private val owner = teamHistoryConfig.owner
  private val pagingLimit = gitHubHistoryConfig.pagingLimit

  suspend fun fetchDiscussion(number: Int): GitHubDiscussion {
    val query = GetDiscussionQuery(
      owner = teamHistoryConfig.owner,
      repository = repository,
      number = number,
    )

    val discussionRaw = graphQlClient.query(query).execute().dataOrThrow().repository?.discussion
      ?: error("Failed to fetch discussion #$number from $owner/$repository")

    return discussionRaw.toGitHubDiscussion()
      .withFetchedComments()
  }

  suspend fun fetchDiscussions(): List<GitHubDiscussion> {
    var lastCursor: String? = null
    val allDiscussions = mutableListOf<GitHubDiscussion>()
    paging@ do {
      val query = GetDiscussionsPageQuery(
        owner = owner,
        repository = repository,
        afterCursor = Optional.presentIfNotNull(lastCursor),
        pageSize = Optional.present(pagingLimit),
        orderBy = DiscussionOrder(DiscussionOrderField.CREATED_AT, OrderDirection.DESC),
      )

      val discussionsPage = graphQlClient.query(query).execute().dataOrThrow().repository?.discussionsPage
        ?: error("Failed to fetch discussions from $owner/$repository")

      val totalDiscussionsFetched = discussionsPage.discussions.orEmpty().size
      printableLn().using(gitHubHistoryConfig)
      printable("... fetched $totalDiscussionsFetched discussions").using(gitHubHistoryConfig)

      val discussionsRaw = discussionsPage.discussions?.filterNotNull().orEmpty()
      val discussions = discussionsRaw.map(GetDiscussionsPageQuery.Discussion::toGitHubDiscussion)

      // the API list is sorted by date descending, so checking PRs sequentially could break the loop faster
      for (discussion in discussions) {
        printable(".", indent = 0).using(gitHubHistoryConfig)
        // fetched discussions still haven't reached the target end date, so we skip
        if (discussion.createdAt.date > teamHistoryConfig.endDate) continue
        // fetched discussions have passed the target start date, so we stop looping
        if (discussion.createdAt.date < teamHistoryConfig.startDate) break@paging
        // fetched discussions are within the target date range, so we add each of them
        allDiscussions += discussion
      }

      lastCursor = if (discussionsPage.pageInfo.hasNextPage) {
        discussionsPage.pageInfo.endCursor
      } else null
    } while (lastCursor != null)

    printableLn().using(gitHubHistoryConfig)
    printableLn("... now enriching ${allDiscussions.size} discussions from $owner/$repository...")

    return allDiscussions
      .parallelMap { discussion ->
        discussion
          .withFetchedComments()
      }
      .sortedBy(GitHubDiscussion::createdAt)
  }

  private suspend fun GitHubDiscussion.withFetchedComments(): GitHubDiscussion {
    var lastCursor: String? = null
    val allComments = mutableListOf<GitHubDiscussion.Comment>()
    do {
      val query = GetDiscussionCommentsPageQuery(
        owner = owner,
        repository = repository,
        number = number,
        afterCursor = Optional.presentIfNotNull(lastCursor),
        pageSize = Optional.present(pagingLimit),
      )

      val commentsPage = graphQlClient.query(query).execute().dataOrThrow().repository?.discussion?.commentsPage
        ?: error("Failed to fetch comments for discussion #$number from $owner/$repository")

      val commentsRaw = commentsPage.comments.orEmpty()
      val totalCommentsFetched = commentsRaw.size
      val totalRepliesFetched = commentsRaw.filterNotNull().flatMap { it.replyPage.replies.orEmpty() }.size

      printableLn(
        "... #$number: fetched $totalCommentsFetched comments and $totalRepliesFetched replies",
        indent = 6,
      ).using(gitHubHistoryConfig)

      allComments += commentsRaw.filterNotNull()
        .flatMap(GetDiscussionCommentsPageQuery.Comment::withRepliesToGitHubComments)

      lastCursor = if (commentsPage.pageInfo.hasNextPage) {
        commentsPage.pageInfo.endCursor
      } else null
    } while (lastCursor != null)

    return copy(comments = allComments.sortedBy(GitHubDiscussion.Comment::createdAt))
  }

  private fun Printable.using(gitHubHistoryConfig: GitHubHistoryConfig) =
    onlyIf(gitHubHistoryConfig.shouldPrintProgress)

  private fun PrintableLn.using(gitHubHistoryConfig: GitHubHistoryConfig) =
    onlyIf(gitHubHistoryConfig.shouldPrintProgress)

}
