import github.GitHubHistory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Config
import vcs.TeamHistory

@OptIn(ExperimentalSerializationApi::class)
fun main() = try {
  println("\n== Code Stats CLI ==\n")

  println("Loading configuration...")
  val config = Config.fromFile("src/commonMain/resources/sample.config.yaml")

  println("Loading team history...")
  val history: TeamHistory = GitHubHistory(config)

  runBlocking {
    val chosenRepo = config.teams.first().discussionRepositories.first()
    val fullRepo = history.fetchRepository(chosenRepo)
    val truncated = fullRepo.truncate()
    val json = Json {
      prettyPrint = true
      encodeDefaults = true
      prettyPrintIndent = "  "
    }
    println(json.encodeToString(truncated))
  }
  history.clean()
} catch (e: Throwable) {
  println("Error: ${e.message}")
  e.printStackTrace()
}
