package github

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.http.LoggingInterceptor.Level
import com.github.graphql.GetDiscussionCommentsPageQuery
import com.github.graphql.GetDiscussionQuery
import com.github.graphql.GetDiscussionsPageQuery
import com.github.graphql.type.DiscussionOrder
import com.github.graphql.type.DiscussionOrderField
import com.github.graphql.type.OrderDirection
import github.mapping.toGeneric
import github.mapping.toGitHubDiscussion
import github.mapping.withRepliesToGitHubComments
import github.models.GitHubDiscussion
import github.models.GitHubPullRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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
import kotlinx.serialization.json.Json
import models.CodeReview
import models.Config
import models.Discussion
import models.Repository
import parallelMap
import readEnvVar
import vcs.TeamHistory

class GitHubHistory(
  override val config: Config,
  overrideBaseRestUrL: String? = null,
  overrideBaseGraphQlUrL: String? = null,
  overrideAuthToken: String? = null,
  overridePagingLimit: Int? = null,
  overrideIsVerbose: Boolean? = null,
  overridePrintProgress: Boolean? = null,
) : TeamHistory {

  private val authToken = (overrideAuthToken ?: readEnvVar("GITHUB_TOKEN"))
    ?.trim()?.takeIf { it.isNotEmpty() }
    ?: throw IllegalStateException("GITHUB_TOKEN is not set")

  private val isVerbose = (overrideIsVerbose ?: readEnvVar("GITHUB_VERBOSE")?.toBoolean())
    ?: false

  private val baseUrl = (overrideBaseRestUrL ?: readEnvVar("GITHUB_URL"))
    ?.trim()?.takeIf { it.isNotEmpty() }
    ?: "https://api.github.com"

  private val baseGraphQlUrl = (overrideBaseGraphQlUrL ?: readEnvVar("GITHUB_GRAPHQL_URL"))
    ?.trim()?.takeIf { it.isNotEmpty() }
    ?: "https://api.github.com/graphql"

  private val pagingLimit = (overridePagingLimit ?: readEnvVar("GITHUB_PAGING_LIMIT")?.toIntOrNull())
    ?.takeIf { it in 1..100 }
    ?: 100

  private val printProgress = (overridePrintProgress ?: readEnvVar("GITHUB_PROGRESS")?.toBoolean())
    ?: true

  private val jsonSerializer = Json {
    useAlternativeNames = false
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
  }

  private val http = HttpClient(CIO) {
    engine {
      threadsCount = 2 // rate limiter will kill us otherwise
      requestTimeout = 10_000 // 10 seconds per request is more than enough
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

  private val graphQlAuthorizationInterceptor = object : HttpInterceptor {
    override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
      val newRequest = request.newBuilder().addHeader(HttpHeaders.Authorization, "Bearer $authToken").build()
      return chain.proceed(newRequest)
    }
  }

  private val graphQl = ApolloClient.Builder()
    .serverUrl(baseGraphQlUrl)
    .addHttpInterceptor(graphQlAuthorizationInterceptor)
    .addHttpInterceptor(LoggingInterceptor(level = if (isVerbose) Level.BODY else Level.NONE))
    .build()

  override suspend fun fetchCodeReview(repository: String, number: Int): CodeReview {
    val url = repository.endpoints.pullRequest(number)
    val response = http.get(url)
    if (response.status.value !in 200..299) error(
      "Failed to fetch data from '$url'.\n" +
        "  [${response.status.value}] ${response.bodyAsText()}\n"
    )
    val pullRequest = response.body<GitHubPullRequest>()
    printlnProgress("\nEnriching PR #$number from ${config.owner}/$repository")

    return pullRequest
      .withFetchedReviews(repository)
      .withFetchedComments(repository)
      .withFetchedFiles(repository)
      .toGeneric()
  }

  override suspend fun fetchCodeReviews(repository: String): List<CodeReview> {
    var page = 1
    val url = repository.endpoints.pullRequests
    val allRequests = mutableListOf<GitHubPullRequest>()
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
      val pagePullRequests = response.body<List<GitHubPullRequest>>()
      printProgress(
        "\n... fetched ${pagePullRequests.size} PRs from page $page. " +
          if (pagePullRequests.isEmpty()) "" else "Latest PR date: ${pagePullRequests.last().createdAt}"
      )
      if (pagePullRequests.isEmpty()) break // no more results

      // the API list is sorted by date descending, so checking PRs sequentially could break the loop faster
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

    printlnProgress("\nEnriching ${allRequests.size} PRs from ${config.owner}/$repository...")
    return allRequests
      .parallelMap { request ->
        request
          .withFetchedReviews(repository)
          .withFetchedComments(repository)
          .withFetchedFiles(repository)
      }
      .sortedBy(GitHubPullRequest::createdAt)
      .map(GitHubPullRequest::toGeneric)
  }

  override suspend fun fetchDiscussion(repository: String, number: Int): Discussion {
    val query = GetDiscussionQuery(
      owner = config.owner,
      repository = repository,
      number = number,
    )

    val discussionRaw = graphQl.query(query).execute().dataOrThrow().repository?.discussion
      ?: error("Failed to parse discussion #$number from ${config.owner}/$repository")

    return discussionRaw.toGitHubDiscussion()
      .withFetchedComments(repository)
      .toGeneric()
  }

  override suspend fun fetchDiscussions(repository: String): List<Discussion> {
    var lastCursor: String? = null
    val allDiscussions = mutableListOf<GitHubDiscussion>()
    paging@ do {
      val query = GetDiscussionsPageQuery(
        owner = config.owner,
        repository = repository,
        afterCursor = Optional.presentIfNotNull(lastCursor),
        pageSize = Optional.present(pagingLimit),
        orderBy = DiscussionOrder(DiscussionOrderField.CREATED_AT, OrderDirection.DESC),
      )

      val discussionsPage = graphQl.query(query).execute().dataOrThrow().repository?.discussionsPage
        ?: error("Failed to fetch discussions from ${config.owner}/$repository")

      val totalDiscussionsFetched = discussionsPage.discussions.orEmpty().size
      printProgress("\n... fetched $totalDiscussionsFetched discussions")

      val discussionsRaw = discussionsPage.discussions?.filterNotNull().orEmpty()
      val discussions = discussionsRaw.map { it.toGitHubDiscussion() }

      // the API list is sorted by date descending, so checking PRs sequentially could break the loop faster
      for (discussion in discussions) {
        printProgress(".", indent = 0)
        // fetched discussions still haven't reached the target end date, so we skip
        if (discussion.createdAt.date > config.endDate) continue
        // fetched discussions have passed the target start date, so we stop looping
        if (discussion.createdAt.date < config.startDate) break@paging
        // fetched discussions are within the target date range, so we add each of them
        allDiscussions += discussion
      }

      lastCursor = if (discussionsPage.pageInfo.hasNextPage) {
        discussionsPage.pageInfo.endCursor
      } else null
    } while (lastCursor != null)

    printlnProgress("\nEnriching ${allDiscussions.size} discussions from ${config.owner}/$repository...")
    return allDiscussions
      .parallelMap { discussion ->
        discussion
          .withFetchedComments(repository)
      }
      .sortedBy(GitHubDiscussion::createdAt)
      .map(GitHubDiscussion::toGeneric)
  }

  override suspend fun fetchRepository(
    repository: String,
    excludeCodeReviews: Boolean,
    excludeDiscussions: Boolean,
  ): Repository {
    printlnProgress("Fetching ${config.owner}/$repository...")
    val codeReviews = if (!excludeCodeReviews) fetchCodeReviews(repository) else emptyList()
    val discussions = if (!excludeDiscussions) fetchDiscussions(repository) else emptyList()
    return Repository(
      owner = config.owner,
      name = repository,
      codeReviews = codeReviews,
      discussions = discussions,
    )
  }

  override fun clean() {
    http.close()
    graphQl.close()
  }

  private suspend fun GitHubPullRequest.withFetchedReviews(repository: String): GitHubPullRequest {
    var page = 1
    val url = repository.endpoints.pullRequestReviews(number)
    val allReviews = mutableListOf<GitHubPullRequest.Review>()

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
      val pageReviews = response.body<List<GitHubPullRequest.Review>>()
      printlnProgress("... #$number: fetched ${pageReviews.size} reviews from page $page", indent = 6)
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
      .sortedBy(GitHubPullRequest.Review::submittedAt) // sort by date (oldest review first)

    return copy(reviews = finalReviews)
  }

  private suspend fun GitHubPullRequest.withFetchedComments(repository: String): GitHubPullRequest {
    var page = 1
    val url = repository.endpoints.pullRequestComments(number)
    val allComments = mutableListOf<GitHubPullRequest.Comment>()

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
      val pageComments = response.body<List<GitHubPullRequest.Comment>>()
      printlnProgress("... #$number: fetched ${pageComments.size} comments from page $page", indent = 6)
      if (pageComments.isEmpty()) break // no more results

      page++
      allComments.addAll(pageComments)
    }

    return copy(comments = allComments.sortedBy(GitHubPullRequest.Comment::createdAt))
  }

  private suspend fun GitHubPullRequest.withFetchedFiles(repository: String): GitHubPullRequest {
    var page = 1
    val url = repository.endpoints.pullRequestFiles(number)
    val allFiles = mutableListOf<GitHubPullRequest.File>()

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
      val pageFiles = response.body<List<GitHubPullRequest.File>>()
      printlnProgress("... #$number: fetched ${pageFiles.size} files from page $page", indent = 6)
      if (pageFiles.isEmpty()) break // no more results

      page++
      allFiles.addAll(pageFiles)
    }

    return copy(files = allFiles.sortedBy(GitHubPullRequest.File::fileName))
  }

  private suspend fun GitHubDiscussion.withFetchedComments(repository: String): GitHubDiscussion {
    var lastCursor: String? = null
    val allComments = mutableListOf<GitHubDiscussion.Comment>()
    do {
      val query = GetDiscussionCommentsPageQuery(
        owner = config.owner,
        repository = repository,
        number = number,
        afterCursor = Optional.presentIfNotNull(lastCursor),
        pageSize = Optional.present(pagingLimit),
      )

      val commentsPage = graphQl.query(query).execute().dataOrThrow().repository?.discussion?.commentsPage
        ?: error("Failed to fetch comments for discussion #$number from ${config.owner}/$repository")
      val commentsRaw = commentsPage.comments.orEmpty()

      val totalCommentsFetched = commentsRaw.size
      val totalRepliesFetched = commentsRaw.filterNotNull().flatMap { it.replyPage.replies.orEmpty() }.size
      printlnProgress(
        "\n... #$number: fetched $totalCommentsFetched comments and $totalRepliesFetched replies",
        indent = 6,
      )

      allComments += commentsRaw.filterNotNull()
        .flatMap(GetDiscussionCommentsPageQuery.Comment::withRepliesToGitHubComments)

      lastCursor = if (commentsPage.pageInfo.hasNextPage) {
        commentsPage.pageInfo.endCursor
      } else null
    } while (lastCursor != null)

    return copy(comments = allComments.sortedBy(GitHubDiscussion.Comment::createdAt))
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

  private fun printlnProgress(text: String = "", indent: Int = 3) =
    if (printProgress) println("${" ".repeat(indent)}$text") else Unit

  private fun printProgress(text: String = "", indent: Int = 3) =
    if (printProgress) print("${" ".repeat(indent)}$text") else Unit

}
