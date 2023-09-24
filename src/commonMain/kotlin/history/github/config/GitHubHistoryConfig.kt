package history.github.config

import kotlin.math.roundToLong
import utils.readEnvVar

private const val GITHUB_MAX_RPH = 15_000L // 15k requests per hour
private const val GITHUB_MAX_RPM = GITHUB_MAX_RPH / 60.0 // ~250 requests per minute
private const val GITHUB_MAX_RPS = GITHUB_MAX_RPM / 60.0 // ~4 requests per second
private const val PARALLEL_REQUESTS = 2L // 2 coroutines running in parallel
private val REQUEST_DELAY_DEFAULT = ((GITHUB_MAX_RPS / PARALLEL_REQUESTS) * 1000).roundToLong() // ~2100 ms delay

data class GitHubHistoryConfig(
  val baseRestUrl: String =
    readEnvVar("GITHUB_URL")
      ?.trim()?.takeIf(String::isNotEmpty)
      ?: "https://api.github.com",

  val baseGraphQlUrl: String =
    readEnvVar("GITHUB_GRAPHQL_URL")
      ?.trim()?.takeIf(String::isNotEmpty)
      ?: "https://api.github.com/graphql",

  val authToken: String =
    readEnvVar("GITHUB_TOKEN")
      ?.trim()?.takeIf(String::isNotEmpty)
      ?: throw IllegalStateException("\$GITHUB_TOKEN is not set"),

  val pagingLimit: Int =
    readEnvVar("GITHUB_PAGING_LIMIT")?.toIntOrNull()
      ?.takeIf { it in 1..100 }
      ?: 100,

  val isVerbose: Boolean =
    readEnvVar("GITHUB_VERBOSE")?.toBoolean()
      ?: false,

  val shouldPrintProgress: Boolean =
    readEnvVar("GITHUB_PRINT_PROGRESS")?.toBoolean()
      ?: true,

  // https://docs.github.com/en/rest/overview/resources-in-the-rest-api?apiVersion=2022-11-28#rate-limits-for-requests-from-personal-accounts

  val rateLimitDelayMillis: Long =
    readEnvVar("GITHUB_RATE_LIMIT_DELAY_MILLIS")?.toLongOrNull()
      ?.takeIf { it > 0 }
      ?: REQUEST_DELAY_DEFAULT,
)
