package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class LocalDateTimeIsBetweenTest {

  @Test fun `isBetween returns true when date is equal to start date`() {
    val dateTime = LocalDateTime(2023, 2, 28, 10, 40, 20)
    val rangeStart = LocalDate(2023, 2, 28)
    val rangeEnd = null

    assertTrue(dateTime.isBetween(rangeStart, rangeEnd))
  }

  @Test fun `isBetween returns true when date is equal to end date`() {
    val dateTime = LocalDateTime(2023, 2, 28, 10, 40, 20)
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 2, 28)

    assertTrue(dateTime.isBetween(rangeStart, rangeEnd))
  }

  @Test fun `isBetween returns true when date is between start and end date`() {
    val dateTime = LocalDateTime(2023, 2, 28, 10, 40, 20)
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    assertTrue(dateTime.isBetween(rangeStart, rangeEnd))
  }

  @Test fun `isBetween returns true when date is after start and end is null`() {
    val dateTime = LocalDateTime(2023, 2, 28, 10, 40, 20)
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    assertTrue(dateTime.isBetween(rangeStart, rangeEnd))
  }

  @Test fun `isBetween returns false when date is before start`() {
    val dateTime = LocalDateTime(2023, 2, 28, 10, 40, 20)
    val rangeStart = LocalDate(2023, 3, 1)
    val rangeEnd = null

    assertFalse(dateTime.isBetween(rangeStart, rangeEnd))
  }

  @Test fun `isBetween returns false when date is after end`() {
    val dateTime = LocalDateTime(2023, 2, 28, 10, 40, 20)
    val rangeStart = LocalDate(2023, 2, 26)
    val rangeEnd = LocalDate(2023, 2, 27)

    assertFalse(dateTime.isBetween(rangeStart, rangeEnd))
  }

}
