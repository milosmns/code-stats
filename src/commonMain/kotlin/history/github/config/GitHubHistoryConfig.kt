package history.github.config

import utils.readEnvVar

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
    readEnvVar("GITHUB_PROGRESS")?.toBoolean()
      ?: true,

  val rateLimitDelayMillis: Long =
    readEnvVar("GITHUB_RATE_LIMIT_DELAY_MILLIS")?.toLongOrNull()
      ?.takeIf { it > 0 }
      ?: 200L,
)
