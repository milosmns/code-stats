package calculator

import components.data.Repository
import components.metrics.CodeReviews
import history.filter.transform.RepositoryCreatedAtTransform
import kotlinx.datetime.LocalDate

class CodeReviewsCalculator : GenericCountMetricCalculator<CodeReviews> {

  override fun calculate(repositories: List<Repository>): CodeReviews {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) -> codeReviews.count().toLong() }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .toSet()
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .count { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .toLong()
      }

    val perRepository = repositories
      .associateWith { repository -> repository.codeReviews.count().toLong() }

    return CodeReviews(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perRepository = perRepository,
    )
  }

  override fun getTimeSeriesTransform(date: LocalDate) = RepositoryCreatedAtTransform(
    createdAt = date,
    applyToCommentsAndFeedbacks = false,
    applyToCodeReviewsAndDiscussions = true,
  )

}
