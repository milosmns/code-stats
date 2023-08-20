package calculator

import components.data.Repository
import components.metrics.CodeReviewFeedbacksTotal

class CodeReviewFeedbacksTotalCalculator : GenericLongMetricCalculator<CodeReviewFeedbacksTotal> {

  override fun calculate(repositories: List<Repository>): CodeReviewFeedbacksTotal {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.feedbacks.countFeedbacksTotal() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.feedbacks }
      .groupBy { feedback -> feedback.author }
      .mapValues { (_, feedbacks) -> feedbacks.countFeedbacksTotal() }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.feedbacks.countFeedbacksTotal() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.feedbacks.countFeedbacksTotal() }
      }

    return CodeReviewFeedbacksTotal(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

}
