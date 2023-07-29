import calculator.di.provideCycleTimeCalculator
import components.data.Repository
import components.data.TeamHistoryConfig
import history.TeamHistory
import history.github.di.provideGitHubHistory
import history.github.di.provideGitHubHistoryConfig
import history.storage.di.provideStoredHistory
import kotlin.math.roundToLong
import kotlinx.coroutines.runBlocking
import utils.durationString
import utils.fromFile

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
    storage.purgeAll()
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
    val stored = storedUnsorted.sorted()

    // OTHER EXPERIMENTS
    val cycleTimeCalculator = provideCycleTimeCalculator()
    val cycleTime = cycleTimeCalculator.calculate(stored)
    println("== CYCLE TIME ==")
    println("Average per author: ${cycleTime.averagePerAuthor.roundToLong().durationString}")
    println("Average per reviewer: ${cycleTime.averagePerReviewer.roundToLong().durationString}")
    println("Average per codeReview: ${cycleTime.averagePerCodeReview.roundToLong().durationString}")
    println("Average per repository: ${cycleTime.averagePerRepository.roundToLong().durationString}")
    println("-- CYCLE TIME --")
  } catch (e: Throwable) {
    println("CRITICAL FAILURE! \n\n * ${e.message} * \n\n")
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
