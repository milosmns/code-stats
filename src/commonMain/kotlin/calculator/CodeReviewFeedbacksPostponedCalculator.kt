package calculator

import components.data.Repository
import components.metrics.CodeReviewFeedbacksPostponed

class CodeReviewFeedbacksPostponedCalculator : GenericLongMetricCalculator<CodeReviewFeedbacksPostponed> {

  override fun calculate(repositories: List<Repository>): CodeReviewFeedbacksPostponed {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.feedbacks.countPostponements() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.feedbacks }
      .groupBy { feedback -> feedback.author }
      .mapValues { (_, feedbacks) -> feedbacks.countPostponements() }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.feedbacks.countPostponements() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.feedbacks.countPostponements() }
      }

    return CodeReviewFeedbacksPostponed(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

}
