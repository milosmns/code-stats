package history.github.mapping

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.graphql.GetDiscussionCommentsPageQuery
import com.github.graphql.GetDiscussionQuery
import com.github.graphql.GetDiscussionsPageQuery
import history.github.models.GitHubDiscussion
import history.github.models.GitHubUser
import kotlin.test.Test
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class ToGitHubMappingsTest {

  private val fixedDateTime = LocalDateTime(2023, 6, 1, 15, 45, 0, 0)

  @Test fun `GraphQL user to GitHub user`() {
    val result = graphQlDiscussionAuthor().toGitHubUser()
    val expected = gitHubUser()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL discussion to GitHub discussion`() {
    val result = graphQlDiscussion().toGitHubDiscussion()
    val expected = gitHubDiscussion().copy(comments = emptyList())

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL comment author to GitHub user`() {
    val result = graphQlCommentAuthor().toGitHubUser()
    val expected = gitHubUser()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL comment to GitHub comments`() {
    val result = graphQlComment().withRepliesToGitHubComments()
    val expected = listOf(gitHubComment(), gitHubComment(idOverride = "idr")) // second comment is actually a reply

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL discussion page author to GitHub user`() {
    val result = graphQlDiscussionsPageAuthor().toGitHubUser()
    val expected = gitHubUser()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL discussion page discussion to GitHub discussion`() {
    val result = graphQlDiscussionsPageDiscussion().toGitHubDiscussion()
    val expected = gitHubDiscussion().copy(comments = emptyList())

    assertThat(result).isEqualTo(expected)
  }

  // region Helpers

  private fun gitHubUser() = GitHubUser(
    login = "author",
  )

  private fun gitHubDiscussion() = GitHubDiscussion(
    id = "idd",
    number = 1,
    title = "title",
    body = "body",
    author = gitHubUser(),
    createdAt = fixedDateTime,
    closedAt = fixedDateTime,
    comments = listOf(gitHubComment()),
  )

  private fun gitHubComment(idOverride: String? = null) = GitHubDiscussion.Comment(
    id = idOverride ?: "idc",
    body = "body",
    author = gitHubUser(),
    createdAt = fixedDateTime,
  )

  private fun graphQlDiscussionAuthor() = GetDiscussionQuery.Author(
    login = "author",
  )

  private fun graphQlDiscussion() = GetDiscussionQuery.Discussion(
    id = "idd",
    number = 1,
    title = "title",
    body = "body",
    author = graphQlDiscussionAuthor(),
    createdAt = fixedDateTime.toInstant(TimeZone.UTC).toString(),
    closedAt = fixedDateTime.toInstant(TimeZone.UTC).toString(),
  )

  private fun graphQlComment() = GetDiscussionCommentsPageQuery.Comment(
    id = "idc",
    body = "body",
    commentAuthor = graphQlCommentAuthor(),
    createdAt = fixedDateTime.toInstant(TimeZone.UTC).toString(),
    replyPage = graphQlReplyPage(),
  )

  private fun graphQlReplyPage() = GetDiscussionCommentsPageQuery.ReplyPage(
    replies = listOf(graphQlReply()),
  )

  private fun graphQlReply() = GetDiscussionCommentsPageQuery.Reply(
    id = "idr",
    body = "body",
    replyAuthor = graphQlReplyAuthor(),
    createdAt = fixedDateTime.toInstant(TimeZone.UTC).toString(),
  )

  private fun graphQlDiscussionsPageDiscussion() = GetDiscussionsPageQuery.Discussion(
    id = "idd",
    number = 1,
    title = "title",
    body = "body",
    author = graphQlDiscussionsPageAuthor(),
    createdAt = fixedDateTime.toInstant(TimeZone.UTC).toString(),
    closedAt = fixedDateTime.toInstant(TimeZone.UTC).toString(),
  )

  private fun graphQlReplyAuthor() = GetDiscussionCommentsPageQuery.ReplyAuthor(
    login = "author",
  )

  private fun graphQlCommentAuthor() = GetDiscussionCommentsPageQuery.CommentAuthor(
    login = "author",
  )

  private fun graphQlDiscussionsPageAuthor() = GetDiscussionsPageQuery.Author(
    login = "author",
  )

  // endregion Helpers

}
