package calculator

import components.data.CodeReview
import components.data.Repository
import components.metrics.CycleTime
import utils.epochMillisecondsUtc
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class CycleTimeCalculator(
  private val now: Instant = Clock.System.now(),
) {

  fun calculate(repositories: List<Repository>): CycleTime {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.getCycleTime() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .sumOf { codeReview -> codeReview.getCycleTime() }
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.getCycleTime() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.getCycleTime() }
      }

    return CycleTime(
      perAuthor = perUser,
      perCodeReview = perCodeReview,
      perReviewer = perReviewer,
      perRepository = perRepository,
    )
  }

  private fun CodeReview.getCycleTime(): Long = when {
    mergedAt != null -> mergedAt.epochMillisecondsUtc - createdAt.epochMillisecondsUtc
    closedAt != null -> closedAt.epochMillisecondsUtc - createdAt.epochMillisecondsUtc
    else -> now.toEpochMilliseconds() - createdAt.epochMillisecondsUtc
  }

}
