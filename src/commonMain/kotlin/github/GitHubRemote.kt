package github

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.cinterop.toKString
import kotlinx.serialization.json.Json
import models.Config
import models.Discussion
import models.MergeRequest
import models.Repository
import parallelMap
import platform.posix.getenv
import vcs.TeamHistory

class GitHubRemote(
  override val config: Config,
  overrideBaseRestUrL: String? = null,
  overrideBaseGraphQlUrL: String? = null,
  overrideAuthToken: String? = null,
  overridePagingLimit: Int? = null,
  overrideIsVerbose: Boolean? = null,
  overridePrintProgress: Boolean? = null,
) : TeamHistory {

  private val authToken = (overrideAuthToken ?: getenv("GITHUB_TOKEN")?.toKString())
    ?.trim()?.takeIf { it.isNotEmpty() }
    ?: throw IllegalStateException("GITHUB_TOKEN is not set")

  private val isVerbose = (overrideIsVerbose ?: getenv("GITHUB_VERBOSE")?.toKString()?.toBoolean())
    ?: false

  private val baseUrl = (overrideBaseRestUrL ?: getenv("GITHUB_URL")?.toKString())
    ?.trim()?.takeIf { it.isNotEmpty() }
    ?: "https://api.github.com"

  private val baseGraphQlUrl = (overrideBaseGraphQlUrL ?: getenv("GITHUB_GRAPHQL_URL")?.toKString())
    ?.trim()?.takeIf { it.isNotEmpty() }
    ?: "https://api.github.com/graphql"

  private val pagingLimit = (overridePagingLimit ?: getenv("GITHUB_PAGING_LIMIT")?.toKString()?.toIntOrNull())
    ?.takeIf { it in 1..100 }
    ?: 100

  private val printProgress = (overridePrintProgress ?: getenv("GITHUB_PROGRESS")?.toKString()?.toBoolean())
    ?: true

  private val jsonSerializer = Json {
    useAlternativeNames = false
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
  }

  private val http = HttpClient(Curl) {
    engine {
      threadsCount = 2 // rate limiter will kill us otherwise
    }
    install(Logging) {
      logger = Logger.DEFAULT
      level = if (isVerbose) LogLevel.ALL else LogLevel.NONE
    }
    install(Auth) {
      bearer {
        loadTokens {
          BearerTokens(authToken, "<fake_refresh_token>")
        }
      }
    }
    install(ContentNegotiation) {
      json(jsonSerializer)
    }
    headers {
      append(HttpHeaders.Accept, "application/vnd.github+json")
      append(HttpHeaders.UserAgent, "CodeStatsCLI")
      append("X-GitHub-Api-Version", "2022-11-28")
    }
  }

  override suspend fun fetchMergeRequest(repository: String, number: Int): MergeRequest {
    val url = repository.endpoints.pullRequest(number)
    val response = http.get(url)
    if (response.status.value !in 200..299) error(
      "Failed to fetch data from '$url'.\n" +
        "  [${response.status.value}] ${response.bodyAsText()}\n"
    )
    val pullRequest = response.body<MergeRequest>()
    printlnProgress("\nEnriching PR #$number from ${config.owner}/$repository")

    return pullRequest
      .withFetchedReviews(repository)
      .withFetchedComments(repository)
      .withFetchedFiles(repository)
  }

  override suspend fun fetchDiscussion(repository: String, number: Int): Discussion {
    TODO("Not yet implemented")
  }

  override suspend fun fetchMergeRequestsByDate(repository: String): List<MergeRequest> {
    var page = 1
    val url = repository.endpoints.pullRequests
    val allRequests = mutableListOf<MergeRequest>()
    paging@ while (true) {
      val response = http.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit.toString())
            append("state", "all")
            append("sort", "created")
            append("direction", "desc")
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch data from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )
      val pagePullRequests = response.body<List<MergeRequest>>()
      printProgress(
        "\n... fetched ${pagePullRequests.size} PRs from page $page. " +
          if (pagePullRequests.isEmpty()) "" else "Latest PR date: ${pagePullRequests.last().createdAt}. "
      )
      if (pagePullRequests.isEmpty()) break // no more results

      // the API list is sorted by date descending, so checking PRs sequentially could break optimize looping
      for (pr in pagePullRequests) {
        printProgress(".", indent = 0)
        // fetched PRs still haven't reached the target end date, so we skip
        if (pr.createdAt.date > config.endDate) continue
        // fetched PRs have passed the target start date, so we stop looping
        if (pr.createdAt.date < config.startDate) break@paging
        // fetched PRs are within the target date range, so we add each of them
        allRequests.add(pr)
      }

      page++
    }

    printlnProgress("\nEnriching ${allRequests.size} PRs from ${config.owner}/$repository…")
    return allRequests
      .parallelMap { request ->
        request
          .withFetchedReviews(repository)
          .withFetchedComments(repository)
          .withFetchedFiles(repository)
      }
      .sortedBy { it.createdAt }
  }

  override suspend fun fetchDiscussionsByDate(repository: String): List<Discussion> {
    TODO("Not yet implemented")
  }

  override suspend fun fetchRepository(repository: String): Repository {
    TODO("Not yet implemented")
  }

  override fun clean() = http.close()

  private suspend fun MergeRequest.withFetchedReviews(repository: String): MergeRequest {
    var page = 1
    val url = repository.endpoints.pullRequestReviews(number)
    val allReviews = mutableListOf<MergeRequest.Review>()

    while (true) {
      val response = http.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit.toString())
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch data from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )
      val pageReviews = response.body<List<MergeRequest.Review>>()
      printlnProgress("... #$number: fetched ${pageReviews.size} reviews from page $page", indent = 4)
      if (pageReviews.isEmpty()) break // no more results

      page++
      allReviews.addAll(pageReviews)
    }

    // request changes + comment + approve = 3 reviews; let's take only the last review for each reviewer
    val finalReviews = allReviews.groupBy { it.author.login } // username -> all reviews
      .mapValues { (_, reviews) ->
        reviews // for each username, let's clean up reviews
          .distinctBy { it.state } // reject multiple reviews of the same kind (multiple approves, etc)
          .filter { it.submittedAt != null } // reject reviews that haven't been submitted yet
          .maxByOrNull { it.submittedAt!! } // take the latest review, which should be the final one
      }
      .values // get a list of reviews that includes only the latest review of each username
      .filterNotNull() // remove usernames that provided no valid reviews (shouldn't happen)
      .sortedBy { it.submittedAt } // sort by date (oldest review first)

    return copy(reviews = finalReviews)
  }

  private suspend fun MergeRequest.withFetchedComments(repository: String): MergeRequest {
    var page = 1
    val url = repository.endpoints.pullRequestComments(number)
    val allComments = mutableListOf<MergeRequest.Comment>()

    while (true) {
      val response = http.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit.toString())
            append("sort", "created")
            append("direction", "desc")
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch data from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )
      val pageComments = response.body<List<MergeRequest.Comment>>()
      printlnProgress("... #$number: fetched ${pageComments.size} comments from page $page.", indent = 4)
      if (pageComments.isEmpty()) break // no more results

      page++
      allComments.addAll(pageComments)
    }

    return copy(comments = allComments.sortedBy { it.createdAt })
  }

  private suspend fun MergeRequest.withFetchedFiles(repository: String): MergeRequest {
    var page = 1
    val url = repository.endpoints.pullRequestFiles(number)
    val allFiles = mutableListOf<MergeRequest.Files>()

    while (true) {
      val response = http.get(url) {
        url {
          with(parameters) {
            append("page", page.toString())
            append("per_page", pagingLimit.toString())
          }
        }
      }
      if (response.status.value !in 200..299) error(
        "Failed to fetch data from '$url', page $page.\n" +
          "  [${response.status.value}] ${response.bodyAsText()}\n"
      )
      val pageFiles = response.body<List<MergeRequest.Files>>()
      printlnProgress("... #$number: fetched ${pageFiles.size} files from page $page.", indent = 4)
      if (pageFiles.isEmpty()) break // no more results

      page++
      allFiles.addAll(pageFiles)
    }

    return copy(files = allFiles.sortedBy { it.fileName })
  }

  private val String.endpoints: Endpoints
    get() = Endpoints(this)

  private inner class Endpoints(repository: String) {
    val pullRequests = "$baseUrl/repos/${config.owner}/$repository/pulls"
    fun pullRequest(number: Int) = "$pullRequests/$number"
    fun pullRequestReviews(number: Int) = "${pullRequest(number)}/reviews"
    fun pullRequestComments(number: Int) = "${pullRequest(number)}/comments"
    fun pullRequestFiles(number: Int) = "${pullRequest(number)}/files"
  }

  private fun printlnProgress(text: String = "", indent: Int = 2) =
    if (printProgress) println("${" ".repeat(indent)}$text") else Unit

  private fun printProgress(text: String = "", indent: Int = 2) =
    if (printProgress) print("${" ".repeat(indent)}$text") else Unit

}
