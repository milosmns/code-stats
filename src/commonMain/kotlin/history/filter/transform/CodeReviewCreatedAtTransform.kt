package history.filter.transform

import components.data.CodeReview
import history.filter.predicate.CodeReviewCommentIsCreatedAt
import history.filter.predicate.CodeReviewFeedbackIsSubmittedAt
import kotlinx.datetime.LocalDate

class CodeReviewCreatedAtTransform(
  private val createdAt: LocalDate,
) : (CodeReview) -> CodeReview {
  override fun invoke(subject: CodeReview) = subject.copy(
    comments = subject.comments.filter(CodeReviewCommentIsCreatedAt(createdAt)),
    feedbacks = subject.feedbacks.filter(CodeReviewFeedbackIsSubmittedAt(createdAt)),
  )
}
