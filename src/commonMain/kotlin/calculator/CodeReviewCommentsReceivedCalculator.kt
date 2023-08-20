package calculator

import components.data.Repository
import components.metrics.CodeReviewCommentsReceived

class CodeReviewCommentsReceivedCalculator : GenericLongMetricCalculator<CodeReviewCommentsReceived> {

  override fun calculate(repositories: List<Repository>): CodeReviewCommentsReceived {
    @Suppress("DuplicatedCode") // false positive from DiscussionCommentsReceivedCalculator
    val perUser = repositories
      .flatMap { repository -> repository.codeReviews }
      .map { codeReview -> codeReview.author }
      .toSet()
      .associateWith { author ->
        // we need to filter out self-comments and count only review comments from others
        repositories
          .flatMap { repository -> repository.codeReviews }
          .filter { codeReview -> codeReview.author == author }
          .flatMap { codeReview -> codeReview.comments }
          .count { comment -> comment.author != author }
          .toLong()
      }

    return CodeReviewCommentsReceived(
      perAuthor = perUser,
    )
  }

}
