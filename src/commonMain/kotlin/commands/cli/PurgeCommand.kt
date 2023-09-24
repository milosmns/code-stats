package commands.cli

import components.data.TeamHistoryConfig
import history.storage.StoredHistory
import kotlinx.coroutines.Runnable

class PurgeCommand(
  private val teamHistoryConfig: TeamHistoryConfig,
  private val storedHistory: StoredHistory,
) : Runnable {

  override fun run() {
    println("== File configuration ==")
    println(teamHistoryConfig.simpleFormat)

    println("\n== Purge ==")
    println("Purging will permanently delete all locally stored data.")
    print("There is no data recovery. Continue? (y/n) ")
    if (readln().lowercase().trim() != "y") {
      println("Aborted. No data was deleted.")
      return
    }
    println("Now purging all locally stored data...")
    storedHistory.purgeAll()
    println("Done.")
  }

}
