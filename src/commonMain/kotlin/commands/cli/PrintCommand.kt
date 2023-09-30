package commands.cli

import calculator.di.provideGenericCountMetricCalculators
import components.data.TeamHistoryConfig
import history.filter.transform.RepositoryDateBetweenTransform
import history.storage.StoredHistory
import kotlinx.coroutines.Runnable

class PrintCommand(
  private val teamHistoryConfig: TeamHistoryConfig,
  private val storedHistory: StoredHistory,
) : Runnable {

  override fun run() {
    println("== File configuration ==")
    println(teamHistoryConfig.simpleFormat)

    println("\n== Metrics ==")
    val storedRepos = storedHistory.fetchAllRepositories().map {
      storedHistory.fetchRepository(
        name = it.name,
        includeCodeReviews = true,
        includeDiscussions = true,
      )
    }
    val transform = RepositoryDateBetweenTransform(teamHistoryConfig.startDate, teamHistoryConfig.endDate)
    val filteredRepos = storedRepos.map(transform)
      .filter { repo -> repo.codeReviews.isNotEmpty() || repo.discussions.isNotEmpty() }
    print("Print details with date filtering applied? (y/n) ")
    val dataSet = if (readln().lowercase().trim() == "y") filteredRepos else storedRepos
    println("OK.\n")

    provideGenericCountMetricCalculators().forEach { calculator ->
      val metric = calculator.calculate(dataSet)
      println(metric.simpleFormat)
      println("-- ${metric.name} --\n")
    }
  }

}
