package github

import Printable
import PrintableLn
import github.models.GitHubPullRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import models.TeamHistoryConfig
import parallelMap
import printable
import printableLn

class GitHubPullRequestFetcher(
  private val repository: String,
  private val teamHistoryConfig: TeamHistoryConfig,
  private val gitHubHistoryConfig: GitHubHistoryConfig,
  private val httpClient: HttpClient,
) {

  private val owner = teamHistoryConfig.owner
  private val pagingLimit = gitHubHistoryConfig.pagingLimit.toString()
  private val endpoints = Endpoints()

  suspend fun fetchPullRequest(number: Int): GitHubPullRequest {
    val url = endpoints.pullRequest(number)
    val response = httpClient.get(url)
    if (response.status.value !in 200..299) error(
      "Failed to fetch pull request from '$url'.\n" +
        "  [${response.status.value}] ${response.bodyAsText()}\n"
    )

    val pullRequest = response.body<GitHubPullRequest>()

    printableLn().using(gitHubHistoryConfig)
    printableLn("... now enriching PR #$number from $owner/$repository...").using(gitHubHistoryConfig)

    return pullRequest
      .withFetchedReviews()
      .withFetchedComments()
      .withFetchedFiles()
  }

  suspend fun fetchPullRequests(): List<GitHubPullRequest> {
    var page = 1
    val url = endpoints.pullRequests
    val allRequests = mutableListOf<GitHubPullRequest>()
    paging@ while (true) {
      val response = httpClient.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit)
            append("state", "all")
            append("sort", "created")
            append("direction", "desc")
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch pull requests from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )

      val pagePullRequests = response.body<List<GitHubPullRequest>>()
      printableLn().using(gitHubHistoryConfig)
      printable(
        "... fetched ${pagePullRequests.size} PRs from page $page. " +
          if (pagePullRequests.isEmpty()) "" else "Latest PR date: ${pagePullRequests.last().createdAt}"
      ).using(gitHubHistoryConfig)

      if (pagePullRequests.isEmpty()) break // no more results

      // the API list is sorted by date descending, so checking PRs sequentially could break the loop faster
      for (pr in pagePullRequests) {
        printable(".", indent = 0).using(gitHubHistoryConfig)
        // fetched PRs still haven't reached the target end date, so we skip
        if (pr.createdAt.date > teamHistoryConfig.endDate) continue
        // fetched PRs have passed the target start date, so we stop looping
        if (pr.createdAt.date < teamHistoryConfig.startDate) break@paging
        // fetched PRs are within the target date range, so we add each of them
        allRequests.add(pr)
      }

      page++
    }

    printableLn().using(gitHubHistoryConfig)
    printableLn("... now enriching ${allRequests.size} PRs from $owner/$repository...")
      .using(gitHubHistoryConfig)

    return allRequests
      .parallelMap { request ->
        request
          .withFetchedReviews()
          .withFetchedComments()
          .withFetchedFiles()
      }
      .sortedBy(GitHubPullRequest::createdAt)
  }

  private suspend fun GitHubPullRequest.withFetchedReviews(): GitHubPullRequest {
    var page = 1
    val url = endpoints.pullRequestReviews(number)
    val allReviews = mutableListOf<GitHubPullRequest.Review>()

    while (true) {
      val response = httpClient.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit)
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch reviews from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )

      val pageReviews = response.body<List<GitHubPullRequest.Review>>()
      printableLn("... #$number: fetched ${pageReviews.size} reviews from page $page", indent = 6)
        .using(gitHubHistoryConfig)

      if (pageReviews.isEmpty()) break // no more results

      page++
      allReviews.addAll(pageReviews)
    }

    // request changes + comment + approve = 3 reviews; let's take only the last review for each reviewer
    val finalReviews = allReviews.groupBy { it.author.login } // username -> all reviews
      .mapValues { (_, reviews) ->
        reviews // for each username, let's clean up reviews
          .distinctBy(GitHubPullRequest.Review::state) // reject multiple reviews of the same kind (multiple approves, etc)
          .filter { it.submittedAt != null } // reject reviews that haven't been submitted yet
          .maxByOrNull { it.submittedAt!! } // take the latest review, which should be the final one
      }
      .values // get a list of reviews that includes only the latest review of each username
      .filterNotNull() // remove usernames that provided no valid reviews (shouldn't happen)
      .sortedBy(GitHubPullRequest.Review::submittedAt) // sort by date (oldest review first)

    return copy(reviews = finalReviews)
  }

  private suspend fun GitHubPullRequest.withFetchedComments(): GitHubPullRequest {
    var page = 1
    val url = endpoints.pullRequestComments(number)
    val allComments = mutableListOf<GitHubPullRequest.Comment>()

    while (true) {
      val response = httpClient.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit)
            append("sort", "created")
            append("direction", "desc")
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch comments from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )

      val pageComments = response.body<List<GitHubPullRequest.Comment>>()
      printableLn("... #$number: fetched ${pageComments.size} comments from page $page", indent = 6)
        .using(gitHubHistoryConfig)

      if (pageComments.isEmpty()) break // no more results

      page++
      allComments.addAll(pageComments)
    }

    return copy(comments = allComments.sortedBy(GitHubPullRequest.Comment::createdAt))
  }

  private suspend fun GitHubPullRequest.withFetchedFiles(): GitHubPullRequest {
    var page = 1
    val url = endpoints.pullRequestFiles(number)
    val allFiles = mutableListOf<GitHubPullRequest.File>()

    while (true) {
      val response = httpClient.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit)
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch files from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )

      val pageFiles = response.body<List<GitHubPullRequest.File>>()
      printableLn("... #$number: fetched ${pageFiles.size} files from page $page", indent = 6)
        .using(gitHubHistoryConfig)

      if (pageFiles.isEmpty()) break // no more results

      page++
      allFiles.addAll(pageFiles)
    }

    return copy(files = allFiles.sortedBy(GitHubPullRequest.File::fileName))
  }

  private inner class Endpoints {
    val pullRequests = "${gitHubHistoryConfig.baseRestUrl}/repos/$owner/$repository/pulls"
    fun pullRequest(number: Int) = "$pullRequests/$number"
    fun pullRequestReviews(number: Int) = "${pullRequest(number)}/reviews"
    fun pullRequestComments(number: Int) = "${pullRequest(number)}/comments"
    fun pullRequestFiles(number: Int) = "${pullRequest(number)}/files"
  }

  private fun Printable.using(gitHubHistoryConfig: GitHubHistoryConfig) =
    onlyIf(gitHubHistoryConfig.shouldPrintProgress)

  private fun PrintableLn.using(gitHubHistoryConfig: GitHubHistoryConfig) =
    onlyIf(gitHubHistoryConfig.shouldPrintProgress)

}
