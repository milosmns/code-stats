package calculator

import components.data.Repository
import components.metrics.CodeReviewCommentsAuthored

class CodeReviewCommentsAuthoredCalculator : GenericLongMetricCalculator<CodeReviewCommentsAuthored> {

  override fun calculate(repositories: List<Repository>): CodeReviewCommentsAuthored {
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.comments }
      .groupBy { comment -> comment.author }
      .mapValues { (_, comments) -> comments.size.toLong() }

    val perReviewer = repositories
      .flatMap { repository -> repository.codeReviews }
      .flatMap { codeReview -> codeReview.requestedReviewers }
      .toSet()
      .associateWith { reviewer ->
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.requestedReviewers.contains(reviewer) }
          .flatMap { codeReview -> codeReview.comments }
          .count { comment -> comment.author == reviewer }
          .toLong()
      }

    val perCodeReview = repositories
      .flatMap { repository -> repository.codeReviews }
      .associateWith { codeReview -> codeReview.comments.count().toLong() }

    val perRepository = repositories
      .associateWith { repository ->
        repository.codeReviews
          .sumOf { codeReview -> codeReview.comments.count().toLong() }
      }

    return CodeReviewCommentsAuthored(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perCodeReview = perCodeReview,
      perRepository = perRepository,
    )
  }

}
