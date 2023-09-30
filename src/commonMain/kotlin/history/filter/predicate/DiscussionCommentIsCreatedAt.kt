package history.filter.predicate

import components.data.DiscussionComment
import kotlinx.datetime.LocalDate

class DiscussionCommentIsCreatedAt(
  private val createdAt: LocalDate,
) : (DiscussionComment) -> Boolean {
  override fun invoke(subject: DiscussionComment) = subject.createdAt.date == createdAt
}
