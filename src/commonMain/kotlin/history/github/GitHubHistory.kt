package history.github

import utils.PrintableLn
import com.apollographql.apollo3.ApolloClient
import components.data.Repository
import components.data.TeamHistoryConfig
import history.TeamHistory
import history.github.config.GitHubHistoryConfig
import history.github.di.provideDiscussionFetcherFor
import history.github.di.providePullRequestFetcherFor
import history.github.mapping.toGeneric
import history.github.models.GitHubDiscussion
import history.github.models.GitHubPullRequest
import io.ktor.client.HttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import utils.printableLn

class GitHubHistory(
  override val teamHistoryConfig: TeamHistoryConfig,
  private val gitHubHistoryConfig: GitHubHistoryConfig,
  private val httpClient: HttpClient,
  private val graphQlClient: ApolloClient,
) : TeamHistory {

  override suspend fun fetchCodeReview(repository: String, number: Int) =
    pullRequestFetcherFor(repository)
      .fetchPullRequest(number)
      .toGeneric()

  override suspend fun fetchCodeReviews(repository: String) =
    pullRequestFetcherFor(repository)
      .fetchPullRequests()
      .map(GitHubPullRequest::toGeneric)

  override suspend fun fetchDiscussion(repository: String, number: Int) =
    discussionFetcherFor(repository)
      .fetchDiscussion(number)
      .toGeneric()

  override suspend fun fetchDiscussions(repository: String) =
    discussionFetcherFor(repository)
      .fetchDiscussions()
      .map(GitHubDiscussion::toGeneric)

  override suspend fun fetchRepository(
    repository: String,
    includeCodeReviews: Boolean,
    includeDiscussions: Boolean,
  ) = coroutineScope {
    printableLn("Fetching ${teamHistoryConfig.owner}/$repository...").using(gitHubHistoryConfig)
    val codeReviews = async { if (includeCodeReviews) fetchCodeReviews(repository) else emptyList() }
    val discussions = async { if (includeDiscussions) fetchDiscussions(repository) else emptyList() }
    Repository(
      owner = teamHistoryConfig.owner,
      name = repository,
      codeReviews = codeReviews.await(),
      discussions = discussions.await(),
    )
  }

  override fun close() {
    httpClient.close()
    graphQlClient.close()
  }

  private fun PrintableLn.using(gitHubHistoryConfig: GitHubHistoryConfig) =
    onlyIf(gitHubHistoryConfig.isVerbose)

  private fun pullRequestFetcherFor(repository: String) =
    providePullRequestFetcherFor(repository, teamHistoryConfig, gitHubHistoryConfig, httpClient)

  private fun discussionFetcherFor(repository: String) =
    provideDiscussionFetcherFor(repository, teamHistoryConfig, gitHubHistoryConfig, graphQlClient)

}
