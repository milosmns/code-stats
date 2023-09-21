package history.filter.predicate

import components.data.CodeReviewComment
import kotlinx.datetime.LocalDate

class CodeReviewCommentIsBetween(
  private val startDateInclusive: LocalDate,
  private val endDateInclusive: LocalDate? = null,
) : (CodeReviewComment) -> Boolean {
  override fun invoke(subject: CodeReviewComment) =
    subject.createdAt.isBetween(startDateInclusive, endDateInclusive)
}
