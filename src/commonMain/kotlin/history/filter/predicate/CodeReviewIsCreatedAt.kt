package history.filter.predicate

import components.data.CodeReview
import kotlinx.datetime.LocalDate

class CodeReviewIsCreatedAt(
  private val createdAt: LocalDate,
) : (CodeReview) -> Boolean {
  override fun invoke(subject: CodeReview): Boolean = subject.createdAt.date == createdAt
}
