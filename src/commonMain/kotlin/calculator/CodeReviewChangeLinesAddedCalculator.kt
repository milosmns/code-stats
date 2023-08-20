package calculator

import components.data.Repository
import components.metrics.CodeReviewChangeLinesAdded

class CodeReviewChangeLinesAddedCalculator : GenericLongMetricCalculator<CodeReviewChangeLinesAdded> {

  override fun calculate(repositories: List<Repository>): CodeReviewChangeLinesAdded {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .groupBy { codeReview -> codeReview.author }
      .mapValues { (_, codeReviews) ->
        codeReviews.sumOf { it.countLinesAdded() }
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .toSet()
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .sumOf { codeReview -> codeReview.countLinesAdded() }
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.countLinesAdded() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.countLinesAdded() }
      }

    return CodeReviewChangeLinesAdded(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

}
