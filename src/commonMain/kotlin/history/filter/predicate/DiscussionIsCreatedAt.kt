package history.filter.predicate

import components.data.Discussion
import kotlinx.datetime.LocalDate

class DiscussionIsCreatedAt(
  private val createdAt: LocalDate,
) : (Discussion) -> Boolean {
  override fun invoke(subject: Discussion): Boolean = subject.createdAt.date == createdAt
}
