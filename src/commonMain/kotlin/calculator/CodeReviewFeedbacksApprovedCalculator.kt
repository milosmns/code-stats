package calculator

import components.data.Repository
import components.metrics.CodeReviewFeedbacksApproved
import history.filter.transform.RepositoryCreatedAtTransform
import kotlinx.datetime.LocalDate

class CodeReviewFeedbacksApprovedCalculator : GenericCountMetricCalculator<CodeReviewFeedbacksApproved> {

  override fun calculate(repositories: List<Repository>): CodeReviewFeedbacksApproved {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.feedbacks.countApprovals() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.feedbacks }
      .groupBy { feedback -> feedback.author }
      .mapValues { (_, feedbacks) -> feedbacks.countApprovals() }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.feedbacks.countApprovals() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.feedbacks.countApprovals() }
      }

    return CodeReviewFeedbacksApproved(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

  override fun getTimeSeriesTransform(date: LocalDate) = RepositoryCreatedAtTransform(
    createdAt = date,
    applyToCommentsAndFeedbacks = true,
    applyToCodeReviewsAndDiscussions = false,
  )

}
