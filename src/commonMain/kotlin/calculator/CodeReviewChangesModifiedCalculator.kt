package calculator

import components.data.Repository
import components.metrics.CodeReviewChangesModified
import history.filter.transform.RepositoryFinishedAtTransform
import kotlinx.datetime.LocalDate

class CodeReviewChangesModifiedCalculator : GenericCountMetricCalculator<CodeReviewChangesModified> {

  override fun calculate(repositories: List<Repository>): CodeReviewChangesModified {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.countChangesModified() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .toSet()
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .sumOf { codeReview -> codeReview.countChangesModified() }
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.countChangesModified() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.countChangesModified() }
      }

    return CodeReviewChangesModified(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

  override fun getTimeSeriesTransform(date: LocalDate) = RepositoryFinishedAtTransform(date)

}
