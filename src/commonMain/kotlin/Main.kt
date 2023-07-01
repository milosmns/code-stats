import github.GitHubHistory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.TeamHistoryConfig
import vcs.TeamHistory

@OptIn(ExperimentalSerializationApi::class)
fun main() = runBlocking {
  println("\n== Code Stats CLI ==\n")

  println("Loading configuration...")
  val teamHistoryConfig = TeamHistoryConfig.fromFile("src/commonMain/resources/sample.config.yaml")
  println("Configuration loaded. $teamHistoryConfig")

  println("Loading team history...")
  val history: TeamHistory = GitHubHistory(teamHistoryConfig)

  try {
    val chosenRepo = teamHistoryConfig.teams.first().discussionRepositories.first()
    val fullRepo = history.fetchRepository(chosenRepo, includeCodeReviews = true, includeDiscussions = true)
    val serializer = Json {
      prettyPrint = true
      encodeDefaults = true
      prettyPrintIndent = "  "
    }
    println(serializer.encodeToString(fullRepo.truncate()))
  } catch (e: Throwable) {
    println("Error: ${e.message}")
    e.printStackTrace()
  } finally {
    history.close()
  }
}
