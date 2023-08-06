package calculator

import components.data.Repository
import components.metrics.CodeReviewChangesTotal

class CodeReviewChangesTotalCalculator : GenericLongMetricCalculator<CodeReviewChangesTotal> {

  override fun calculate(repositories: List<Repository>): CodeReviewChangesTotal {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.countChangesTotal() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .sumOf { codeReview -> codeReview.countChangesTotal() }
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.countChangesTotal() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.countChangesTotal() }
      }

    return CodeReviewChangesTotal(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

}
