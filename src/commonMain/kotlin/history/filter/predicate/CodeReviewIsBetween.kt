package history.filter.predicate

import components.data.CodeReview
import kotlinx.datetime.LocalDate

class CodeReviewIsBetween(
  private val startDateInclusive: LocalDate,
  private val endDateInclusive: LocalDate? = null,
) : (CodeReview) -> Boolean {
  override fun invoke(subject: CodeReview): Boolean {
    val isMergedOnTime = when {
      endDateInclusive == null -> true
      subject.mergedAt == null -> false
      else -> subject.mergedAt.date <= endDateInclusive
    }
    val isClosedOnTime = when {
      endDateInclusive == null -> true
      subject.closedAt == null -> false
      else -> subject.closedAt.date <= endDateInclusive
    }
    val isFinishedOnTime = when {
      subject.mergedAt != null && subject.closedAt == null -> isMergedOnTime
      subject.mergedAt == null && subject.closedAt != null -> isClosedOnTime
      subject.mergedAt != null && subject.closedAt != null -> isMergedOnTime && isClosedOnTime
      else -> isMergedOnTime || isClosedOnTime
    }
    val isCreatedBetween = subject.createdAt.isBetween(startDateInclusive, endDateInclusive)
    return isFinishedOnTime && isCreatedBetween
  }
}
