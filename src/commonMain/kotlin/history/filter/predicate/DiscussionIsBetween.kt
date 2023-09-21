package history.filter.predicate

import components.data.Discussion
import kotlinx.datetime.LocalDate

class DiscussionIsBetween(
  private val startDateInclusive: LocalDate,
  private val endDateInclusive: LocalDate? = null,
) : (Discussion) -> Boolean {
  override fun invoke(subject: Discussion): Boolean {
    val isClosedOnTime = when {
      endDateInclusive == null -> true
      subject.closedAt == null -> false
      else -> subject.closedAt.date <= endDateInclusive
    }
    val isCreatedBetween = subject.createdAt.isBetween(startDateInclusive, endDateInclusive)
    return isClosedOnTime && isCreatedBetween
  }
}
