package history.filter.predicate

import components.data.CodeReview
import kotlinx.datetime.LocalDate

class CodeReviewIsFinishedAt(
  private val createdAt: LocalDate,
) : (CodeReview) -> Boolean {
  override fun invoke(subject: CodeReview): Boolean =
    subject.closedAt?.date == createdAt || subject.mergedAt?.date == createdAt
}
