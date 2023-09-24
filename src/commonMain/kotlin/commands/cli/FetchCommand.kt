package commands.cli

import components.data.Repository
import components.data.TeamHistoryConfig
import history.TeamHistory
import history.storage.StoredHistory
import history.storage.config.StorageConfig
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.runBlocking

class FetchCommand(
  private val teamHistoryConfig: TeamHistoryConfig,
  private val teamHistory: TeamHistory,
  private val storageConfig: StorageConfig,
  private val storedHistory: StoredHistory,
) : Runnable {

  override fun run() = runBlocking {
    println("== File configuration ==")
    println(teamHistoryConfig.simpleFormat)

    println("\n== History fetch ==")
    val fetched = mutableListOf<Repository>()
    try {
      print("This will start a new data fetch and may impact rate limiting. Continue? (y/n) ")
      if (readln().lowercase().trim() != "y") {
        println("Aborted. No requests were executed.")
        return@runBlocking
      }
      teamHistoryConfig.teams.forEach { team ->
        println("Looking into team ${team.title}...")
        team.discussionRepositories.forEach {
          println("Fetching discussion history for $it...")
          fetched += teamHistory.fetchRepository(
            repository = it,
            includeCodeReviews = false,
            includeDiscussions = true,
          )
        }
        team.codeRepositories.forEach {
          println("Fetching code/review history for $it...")
          fetched += teamHistory.fetchRepository(
            repository = it,
            includeCodeReviews = true,
            includeDiscussions = false,
          )
        }
      }
      println("Done. Fetched ${fetched.size} repositories.")
    } catch (t: Throwable) {
      println("Failed to complete the task. Error: ${t.message}")
      return@runBlocking
    } finally {
      teamHistory.close()
    }

    println("\n== History storage ==")
    print("Storing fetched data will overwrite existing data. Continue? (y/n) ")
    if (readln().lowercase().trim() != "y") {
      println("Aborted. If rate limiter is applied to your git remote, wait before trying again.")
      return@runBlocking
    }
    println("Now storing ${fetched.size} repositories locally...")
    fetched.forEach(storedHistory::storeRepositoryDeep)
    println("\nDone. Your fetched data is at ${storageConfig.databasePath}")
  }

}
