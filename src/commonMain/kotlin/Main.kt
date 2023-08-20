import calculator.di.provideGenericLongMetricCalculators
import components.data.TeamHistoryConfig
import history.TeamHistory
import history.github.di.provideGitHubHistory
import history.github.di.provideGitHubHistoryConfig
import history.storage.di.provideStoredHistory
import kotlinx.coroutines.runBlocking
import utils.fromFile

fun main(): Unit = runBlocking {
  println("\n== Code Stats CLI ==\n")

  print("Loading configuration... ")
  val teamHistoryConfig = TeamHistoryConfig.fromFile("src/commonMain/resources/sample.config.yaml")
  println("Done.")

  println(teamHistoryConfig.simpleFormat)

  val history: TeamHistory = provideGitHubHistory(
    teamHistoryConfig = teamHistoryConfig,
    gitHubHistoryConfig = provideGitHubHistoryConfig(),
  )

  try {
    // NETWORK EXPERIMENTS
//    println("Loading team history...")
//    val fetched = mutableListOf<Repository>()
//    teamHistoryConfig.teams.forEach { team ->
//      println("Loading for team ${team.title}...")
//      team.discussionRepositories.forEach { repoName ->
//        println("Loading discussion repository $repoName...")
//        fetched += history.fetchRepository(repoName, includeCodeReviews = false, includeDiscussions = true)
//      }
//      team.codeRepositories.forEach { repoName ->
//        println("Loading code repository $repoName...")
//        fetched += history.fetchRepository(repoName, includeCodeReviews = true, includeDiscussions = false)
//      }
//    }

    // STORAGE EXPERIMENTS
    val storage = provideStoredHistory(teamHistoryConfig)
//    storage.purgeAll()
//    val storedUnsorted = mutableListOf<Repository>()
//    fetched.forEach {
//      storage.storeRepositoryDeep(it)
//      storedUnsorted += storage.fetchRepository(
//        it.name,
//        includeCodeReviews = true,
//        includeDiscussions = true,
//      )
//    }

    val stored = storage.fetchAllRepositories().map {
      storage.fetchRepository(
        it.name,
        includeCodeReviews = true,
        includeDiscussions = true,
      )
    }

    stored.forEachIndexed { i, repo ->
      println("\n== Repository #$i ==")
      println(repo.simpleFormat)
      println("-- ${repo.fullName} --\n")
    }

    // OTHER EXPERIMENTS
    provideGenericLongMetricCalculators().forEach {
      val metric = it.calculate(stored)
      println("\n== ${metric.name} ==")
      println(metric.simpleFormat)
      println("-- ${metric.name} --\n")
    }
  } catch (e: Throwable) {
    println("CRITICAL FAILURE! \n\n * ${e.message} * \n\n")
    e.printStackTrace()
  } finally {
    history.close()
  }

}
