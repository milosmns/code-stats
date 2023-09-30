package calculator

import components.data.Repository
import components.metrics.CodeReviewChangeLinesTotal
import history.filter.transform.RepositoryFinishedAtTransform
import kotlinx.datetime.LocalDate

class CodeReviewChangeLinesTotalCalculator : GenericCountMetricCalculator<CodeReviewChangeLinesTotal> {

  override fun calculate(repositories: List<Repository>): CodeReviewChangeLinesTotal {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.countLinesTotal() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .toSet()
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .sumOf { codeReview -> codeReview.countLinesTotal() }
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.countLinesTotal() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.countLinesTotal() }
      }

    return CodeReviewChangeLinesTotal(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

  override fun getTimeSeriesTransform(date: LocalDate) = RepositoryFinishedAtTransform(date)

}
