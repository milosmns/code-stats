import history.TeamHistory
import history.github.di.provideGitHubHistory
import history.github.di.provideGitHubHistoryConfig
import history.storage.di.provideStoredHistory
import kotlinx.coroutines.runBlocking
import models.Repository
import models.TeamHistoryConfig

fun main(): Unit = runBlocking {
  println("\n== Code Stats CLI ==\n")

  print("Loading configuration... ")
  val teamHistoryConfig = TeamHistoryConfig.fromFile("src/commonMain/resources/sample.config.yaml")
  println("Done.")

  println("Loading team history...")
  val history: TeamHistory = provideGitHubHistory(
    teamHistoryConfig = teamHistoryConfig,
    gitHubHistoryConfig = provideGitHubHistoryConfig(),
  )

  try {
    // NETWORK EXPERIMENTS
    val fetchedUnsorted = mutableListOf<Repository>()
    teamHistoryConfig.teams.forEach { team ->
      println("Loading for team ${team.title}...")
      team.discussionRepositories.forEach { repoName ->
        println("Loading discussion repository $repoName...")
        fetchedUnsorted += history.fetchRepository(repoName, includeCodeReviews = false, includeDiscussions = true)
      }
      team.codeRepositories.forEach { repoName ->
        println("Loading code repository $repoName...")
        fetchedUnsorted += history.fetchRepository(repoName, includeCodeReviews = true, includeDiscussions = false)
      }
    }

    // STORAGE EXPERIMENTS
    val storage = provideStoredHistory(teamHistoryConfig)
    val storedUnsorted = mutableListOf<Repository>()
    val fetched = fetchedUnsorted.sorted()
    fetched.forEach {
      storage.storeRepositoryDeep(it)
      storedUnsorted += storage.fetchRepository(
        it.name,
        includeCodeReviews = true,
        includeDiscussions = true,
      )
    }

    // COMPARISON EXPERIMENTS
    val stored = storedUnsorted.sorted()
    println("EQUAL ALL = " + (stored == fetched))
  } catch (e: Throwable) {
    println("CRITICAL FAILURE · ${e.message}")
    e.printStackTrace()
  } finally {
    history.close()
  }
}

private fun List<Repository>.sorted() = sortedBy { "${it.owner}-${it.name}" }.map { repo ->
  repo.copy(
    discussions = repo.discussions.sortedBy { it.createdAt }.map { discussion ->
      discussion.copy(
        comments = discussion.comments.sortedBy { it.createdAt },
      )
    },
    codeReviews = repo.codeReviews.sortedBy { it.createdAt }.map { codeReview ->
      codeReview.copy(
        comments = codeReview.comments.sortedBy { it.createdAt },
        changes = codeReview.changes.sortedBy { it.fileName },
        feedbacks = codeReview.feedbacks.sortedBy { it.submittedAt },
      )
    },
  )
}
