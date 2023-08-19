package calculator

import components.data.Repository
import components.metrics.CodeReviewFeedbacksRejected

class CodeReviewFeedbacksRejectedCalculator : GenericLongMetricCalculator<CodeReviewFeedbacksRejected> {

  override fun calculate(repositories: List<Repository>): CodeReviewFeedbacksRejected {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.feedbacks.countRejections() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.feedbacks }
      .groupBy { feedback -> feedback.author }
      .mapValues { (_, feedbacks) -> feedbacks.countRejections() }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.feedbacks.countRejections() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.feedbacks.countRejections() }
      }

    return CodeReviewFeedbacksRejected(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

}
