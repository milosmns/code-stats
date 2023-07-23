import history.TeamHistory
import history.github.di.provideGitHubHistory
import history.github.di.provideGitHubHistoryConfig
import history.storage.di.provideStoredHistory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Repository
import models.TeamHistoryConfig

fun main(): Unit = runBlocking {
  println("\n== Code Stats CLI ==\n")

  println("Loading configuration...")
  val teamHistoryConfig = TeamHistoryConfig.fromFile("src/commonMain/resources/sample.config.yaml")
  println("Configuration loaded. $teamHistoryConfig")

  println("Loading team history...")
  val history: TeamHistory = provideGitHubHistory(
    teamHistoryConfig = teamHistoryConfig,
    gitHubHistoryConfig = provideGitHubHistoryConfig(),
  )

  try {
    // NETWORK EXPERIMENTS
    val allRepos = mutableSetOf<Repository>()
    teamHistoryConfig.teams.forEach { team ->
      println("Loading for team ${team.title}...")
      team.discussionRepositories.forEach { repoName ->
        println("Loading discussion repository $repoName...")
        allRepos += history.fetchRepository(repoName, includeCodeReviews = false, includeDiscussions = true)
      }
      team.codeRepositories.forEach { repoName ->
        println("Loading code repository $repoName...")
        allRepos += history.fetchRepository(repoName, includeCodeReviews = true, includeDiscussions = false)
      }
    }

    // STORAGE EXPERIMENTS
    val storage = provideStoredHistory(teamHistoryConfig)
    allRepos.forEach { storage.storeRepositoryDeep(it) }

    val fetched = mutableSetOf<Repository>()
    allRepos.forEach {
      fetched += storage.fetchRepository(
        it.name,
        includeCodeReviews = true,
        includeDiscussions = true,
      )
    }

    println("Same = ${fetched == allRepos}")
    println("=====")
    Json { prettyPrintIndent = "  "; prettyPrint = true }.encodeToString(allRepos).also(::println)
    println("=====\n")
    Json { prettyPrintIndent = "  "; prettyPrint = true }.encodeToString(fetched).also(::println)
  } catch (e: Throwable) {
    println("CRITICAL FAILURE · ${e.message}")
    e.printStackTrace()
  } finally {
    history.close()
  }
}
