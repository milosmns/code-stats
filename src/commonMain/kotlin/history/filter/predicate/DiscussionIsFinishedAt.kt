package history.filter.predicate

import components.data.Discussion
import kotlinx.datetime.LocalDate

class DiscussionIsFinishedAt(
  private val createdAt: LocalDate,
) : (Discussion) -> Boolean {
  override fun invoke(subject: Discussion): Boolean = subject.closedAt?.date == createdAt
}
