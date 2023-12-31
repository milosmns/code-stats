package calculator

import components.data.Repository
import components.metrics.CycleTime
import history.filter.transform.RepositoryFinishedAtTransform
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

class CycleTimeCalculator(private val now: Instant) : GenericCountMetricCalculator<CycleTime> {

  override fun calculate(repositories: List<Repository>): CycleTime {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.getCycleTime(now) }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .sumOf { codeReview -> codeReview.getCycleTime(now) }
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.getCycleTime(now) }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.getCycleTime(now) }
      }

    return CycleTime(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

  override fun getTimeSeriesTransform(date: LocalDate) = RepositoryFinishedAtTransform(date)

}
