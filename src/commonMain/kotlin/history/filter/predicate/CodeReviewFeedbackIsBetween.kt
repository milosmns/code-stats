package history.filter.predicate

import components.data.CodeReviewFeedback
import kotlinx.datetime.LocalDate

class CodeReviewFeedbackIsBetween(
  private val startDateInclusive: LocalDate,
  private val endDateInclusive: LocalDate? = null,
) : (CodeReviewFeedback) -> Boolean {
  override fun invoke(subject: CodeReviewFeedback) =
    subject.submittedAt?.isBetween(startDateInclusive, endDateInclusive) == true
}
