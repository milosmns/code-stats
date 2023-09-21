package history.filter.predicate

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun LocalDateTime.isBetween(
  startDateInclusive: LocalDate,
  endDateInclusive: LocalDate? = null,
) = when {
  date < startDateInclusive -> false
  endDateInclusive != null && date > endDateInclusive -> false
  else -> true
}
