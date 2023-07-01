package github

import readEnvVar

class GitHubHistoryConfig(
  baseRestUrL: String? = null,
  baseGraphQlUrL: String? = null,
  authToken: String? = null,
  pagingLimit: Int? = null,
  isVerbose: Boolean? = null,
  shouldPrintProgress: Boolean? = null,
) {

  val baseRestUrl = (baseRestUrL ?: readEnvVar("GITHUB_URL"))
    ?.trim()?.takeIf(String::isNotEmpty)
    ?: "https://api.github.com"

  val baseGraphQlUrl = (baseGraphQlUrL ?: readEnvVar("GITHUB_GRAPHQL_URL"))
    ?.trim()?.takeIf(String::isNotEmpty)
    ?: "https://api.github.com/graphql"

  val authToken = (authToken ?: readEnvVar("GITHUB_TOKEN"))
    ?.trim()?.takeIf(String::isNotEmpty)
    ?: throw IllegalStateException("GITHUB_TOKEN is not set")

  val pagingLimit = (pagingLimit ?: readEnvVar("GITHUB_PAGING_LIMIT")?.toIntOrNull())
    ?.takeIf { it in 1..100 }
    ?: 100

  val isVerbose = (isVerbose ?: readEnvVar("GITHUB_VERBOSE")?.toBoolean())
    ?: false

  val shouldPrintProgress = (shouldPrintProgress ?: readEnvVar("GITHUB_PROGRESS")?.toBoolean())
    ?: true

  override fun toString(): String {
    return "${GitHubHistoryConfig::class.simpleName}(\n" +
      "  BaseRestUrl = $baseRestUrl, \n" +
      "  BaseGraphQlUrl = $baseGraphQlUrl, \n" +
      "  AuthToken = $authToken, \n" +
      "  PagingLimit = $pagingLimit, \n" +
      "  IsVerbose = $isVerbose, \n" +
      "  ShouldPrintProgress = $shouldPrintProgress, \n" +
      ")"
  }

}
