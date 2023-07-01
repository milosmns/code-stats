package github

import PrintableLn
import github.di.provideDiscussionFetcherFor
import github.di.provideGraphQlClient
import github.di.provideHttpClient
import github.di.providePullRequestFetcherFor
import github.mapping.toGeneric
import github.models.GitHubDiscussion
import github.models.GitHubPullRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import models.Repository
import models.TeamHistoryConfig
import printableLn
import vcs.TeamHistory

class GitHubHistory(
  override val teamHistoryConfig: TeamHistoryConfig,
  private val gitHubHistoryConfig: GitHubHistoryConfig = GitHubHistoryConfig(),
) : TeamHistory {

  private val httpClient = provideHttpClient(gitHubHistoryConfig)
  private val graphQlClient = provideGraphQlClient(gitHubHistoryConfig)

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
