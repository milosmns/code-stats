import github.GitHubRemote
import kotlinx.coroutines.runBlocking
import models.Config
import vcs.TeamHistory

fun main() = try {
  println("\n== Code Stats CLI ==\n")

  println("Loading configuration…")
  val config = Config.fromFile("src/commonMain/resources/sample.config.yaml")

  println("Loading team history…")
  val vcs: TeamHistory = GitHubRemote(config)

  runBlocking {
    val chosenRepo = config.teams.first().codeRepositories.first()
    println("Chosen repo ${config.owner}/$chosenRepo\n")

    println("Fetching all PRs from ${config.startDate} to ${config.endDate}…")
    val prs = vcs.fetchMergeRequestsByDate(chosenRepo)
    prs.forEachIndexed { i, ipr ->
      println("PR#${ipr.number} (${i + 1}/${prs.size}): ${ipr.copy(body = ipr.body.cutMiddleTo10())}")
    }
  }
  vcs.clean()
} catch (e: Throwable) {
  println("Error: ${e.message}")
  e.printStackTrace()
}
