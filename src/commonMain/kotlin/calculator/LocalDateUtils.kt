package calculator

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class LocalDateDayIterator(
  startDate: LocalDate,
  private val endDateInclusive: LocalDate,
  private val increment: Long, // in days
) : Iterator<LocalDate> {

  private var currentDate = startDate

  override fun hasNext() = currentDate <= endDateInclusive

  override fun next(): LocalDate {
    val next = currentDate
    currentDate = currentDate.plus(increment, DateTimeUnit.DAY)
    return next
  }

}

class LocalDateDayIterableRange(
  override val start: LocalDate,
  override val endInclusive: LocalDate,
  private val increment: Long = 1, // in days
) : Iterable<LocalDate>, ClosedRange<LocalDate> {

  override fun iterator() = LocalDateDayIterator(start, endInclusive, increment)

  infix fun step(increment: Long) = LocalDateDayIterableRange(start, endInclusive, increment)

}

operator fun LocalDate.rangeTo(other: LocalDate) = LocalDateDayIterableRange(this, other)
