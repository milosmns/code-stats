package history.filter.predicate

import components.data.DiscussionComment
import kotlinx.datetime.LocalDate

class DiscussionCommentIsBetween(
  private val startDateInclusive: LocalDate,
  private val endDateInclusive: LocalDate? = null,
) : (DiscussionComment) -> Boolean {
  override fun invoke(subject: DiscussionComment) =
    subject.createdAt.isBetween(startDateInclusive, endDateInclusive)
}
