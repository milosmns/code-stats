package commands.cli

import components.data.TeamHistoryConfig
import history.filter.transform.RepositoryDateBetweenTransform
import history.storage.StoredHistory
import kotlinx.coroutines.Runnable

class ReportCommand(
  private val teamHistoryConfig: TeamHistoryConfig,
  private val storedHistory: StoredHistory,
) : Runnable {

  override fun run() {
    println("== File configuration ==")
    println(teamHistoryConfig.simpleFormat)

    println("\n== Local storage ==")
    val storedReposShallow = storedHistory.fetchAllRepositories()
    println("Total repositories stored locally: ${storedReposShallow.size}")
    if (storedReposShallow.isNotEmpty()) {
      println(storedReposShallow.joinToString("\n") { "  · ${it.fullName}" })
    }

    println("\n== Data scope ==")
    val range = "[${teamHistoryConfig.startDate} > ${teamHistoryConfig.endDate}]"
    val storedReposDeep = storedReposShallow.map {
      storedHistory.fetchRepository(
        name = it.name,
        includeCodeReviews = true,
        includeDiscussions = true,
      )
    }
    val transform = RepositoryDateBetweenTransform(teamHistoryConfig.startDate, teamHistoryConfig.endDate)
    val filteredRepos = storedReposDeep.map(transform)
      .filter { repo -> repo.codeReviews.isNotEmpty() || repo.discussions.isNotEmpty() }
    if (filteredRepos.isNotEmpty()) {
      println("Data found for requested dates $range.")
    } else {
      println("No data found for requested dates $range.")
    }

    println("\n== Details ==")
    print("Print details per repository? (y/n) ")
    if (readln().lowercase().trim() == "y") {
      print("Print details with date filtering applied? (y/n) ")
      val dataSet = if (readln().lowercase().trim() == "y") filteredRepos else storedReposDeep
      println("OK.\n")
      if (dataSet.isEmpty()) {
        println("No data found for the given criteria.")
      } else {
        println(dataSet.joinToString("\n") { it.simpleFormat })
      }
    }
  }

}
