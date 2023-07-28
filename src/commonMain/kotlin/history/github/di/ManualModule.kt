package history.github.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import components.data.TeamHistoryConfig
import history.github.GitHubDiscussionFetcher
import history.github.GitHubHistory
import history.github.GitHubPullRequestFetcher
import history.github.config.GitHubHistoryConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Suppress("NO_ACTUAL_FOR_EXPECT") // IDEA bug, ignore
expect fun provideHttpClientEngineFactory(): HttpClientEngineFactory<*>

fun provideGitHubHistory(
  teamHistoryConfig: TeamHistoryConfig,
  gitHubHistoryConfig: GitHubHistoryConfig = provideGitHubHistoryConfig(),
) = GitHubHistory(
  teamHistoryConfig = teamHistoryConfig,
  gitHubHistoryConfig = gitHubHistoryConfig,
  httpClient = provideHttpClient(gitHubHistoryConfig),
  graphQlClient = provideGraphQlClient(gitHubHistoryConfig),
)

fun provideGitHubHistoryConfig(
  overrideBaseRestUrL: String? = null,
  overrideBaseGraphQlUrL: String? = null,
  overrideAuthToken: String? = null,
  overridePagingLimit: Int? = null,
  overrideIsVerbose: Boolean? = null,
  overrideShouldPrintProgress: Boolean? = null,
  overrideRateLimitDelayMillis: Long? = null,
) = GitHubHistoryConfig().let {
  it.copy(
    baseRestUrl = overrideBaseRestUrL ?: it.baseRestUrl,
    baseGraphQlUrl = overrideBaseGraphQlUrL ?: it.baseGraphQlUrl,
    authToken = overrideAuthToken ?: it.authToken,
    pagingLimit = overridePagingLimit ?: it.pagingLimit,
    isVerbose = overrideIsVerbose ?: it.isVerbose,
    shouldPrintProgress = overrideShouldPrintProgress ?: it.shouldPrintProgress,
    rateLimitDelayMillis = overrideRateLimitDelayMillis ?: it.rateLimitDelayMillis,
  )
}

private fun provideJsonSerializer(): Json =
  Json {
    useAlternativeNames = false
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
  }

fun provideHttpClient(gitHubHistoryConfig: GitHubHistoryConfig): HttpClient =
  HttpClient(provideHttpClientEngineFactory()) {
    engine {
      threadsCount = 2 // GitHub rate limiter will kill us otherwise
    }
    install(Logging) {
      logger = Logger.DEFAULT
      level = if (gitHubHistoryConfig.isVerbose) LogLevel.ALL else LogLevel.NONE
    }
    install(Auth) {
      bearer {
        loadTokens {
          BearerTokens(gitHubHistoryConfig.authToken, "<fake_refresh_token>")
        }
      }
    }
    install(ContentNegotiation) {
      json(provideJsonSerializer())
    }
    headers {
      append(HttpHeaders.Accept, "application/vnd.github+json")
      append(HttpHeaders.UserAgent, "CodeStatsCLI")
      append("X-GitHub-Api-Version", "2022-11-28")
    }
  }

private fun provideGraphQlAuthorizationInterceptor(gitHubHistoryConfig: GitHubHistoryConfig) =
  object : HttpInterceptor {
    override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
      val newRequest = request.newBuilder()
        .addHeader(HttpHeaders.Authorization, "Bearer ${gitHubHistoryConfig.authToken}")
        .build()
      return chain.proceed(newRequest)
    }
  }

fun provideGraphQlClient(gitHubHistoryConfig: GitHubHistoryConfig) = ApolloClient.Builder()
  .serverUrl(gitHubHistoryConfig.baseGraphQlUrl)
  .addHttpInterceptor(provideGraphQlAuthorizationInterceptor(gitHubHistoryConfig))
  .addHttpInterceptor(
    LoggingInterceptor(
      level = if (gitHubHistoryConfig.isVerbose) LoggingInterceptor.Level.BODY
      else LoggingInterceptor.Level.NONE
    )
  )
  .build()

fun providePullRequestFetcherFor(
  repository: String,
  teamHistoryConfig: TeamHistoryConfig,
  gitHubHistoryConfig: GitHubHistoryConfig,
  httpClient: HttpClient,
) = GitHubPullRequestFetcher(
  repository = repository,
  teamHistoryConfig = teamHistoryConfig,
  gitHubHistoryConfig = gitHubHistoryConfig,
  httpClient = httpClient,
)

fun provideDiscussionFetcherFor(
  repository: String,
  teamHistoryConfig: TeamHistoryConfig,
  gitHubHistoryConfig: GitHubHistoryConfig,
  graphQlClient: ApolloClient,
) = GitHubDiscussionFetcher(
  repository = repository,
  teamHistoryConfig = teamHistoryConfig,
  gitHubHistoryConfig = gitHubHistoryConfig,
  graphQlClient = graphQlClient,
)
