package calculator

import components.data.Repository
import components.metrics.CodeReviewChangesRemoved

class CodeReviewChangesRemovedCalculator : GenericLongMetricCalculator<CodeReviewChangesRemoved> {

  override fun calculate(repositories: List<Repository>): CodeReviewChangesRemoved {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.countChangesRemoved() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .sumOf { codeReview -> codeReview.countChangesRemoved() }
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.countChangesRemoved() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.countChangesRemoved() }
      }

    return CodeReviewChangesRemoved(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

}
