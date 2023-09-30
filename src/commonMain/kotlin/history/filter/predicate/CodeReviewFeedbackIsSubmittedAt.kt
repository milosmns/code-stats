package history.filter.predicate

import components.data.CodeReviewFeedback
import kotlinx.datetime.LocalDate

class CodeReviewFeedbackIsSubmittedAt(
  private val createdAt: LocalDate,
) : (CodeReviewFeedback) -> Boolean {
  override fun invoke(subject: CodeReviewFeedback) = subject.submittedAt?.date == createdAt
}
