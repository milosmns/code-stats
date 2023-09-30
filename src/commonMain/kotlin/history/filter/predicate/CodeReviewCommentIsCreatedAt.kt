package history.filter.predicate

import components.data.CodeReviewComment
import kotlinx.datetime.LocalDate

class CodeReviewCommentIsCreatedAt(
  private val createdAt: LocalDate,
) : (CodeReviewComment) -> Boolean {
  override fun invoke(subject: CodeReviewComment) = subject.createdAt.date == createdAt
}
