package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class DiscussionIsBetweenTest {

  // region Closed discussion

  @Test fun `closed discussion is between returns false when created date is out of closed range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertFalse(check(discussion))
  }

  @Test fun `closed discussion is between returns false when created date is out of open range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertFalse(check(discussion))
  }

  @Test fun `closed discussion is between returns false when closed date is out of closed range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
      closedAt = LocalDateTime(2023, 3, 2, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertFalse(check(discussion))
  }

  @Test fun `closed discussion is between returns true when created and closed dates are in open range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertTrue(check(discussion))
  }

  @Test fun `closed discussion is between returns true when created and closed dates are in closed range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertTrue(check(discussion))
  }

  // endregion Closed discussion

  // region Open discussion

  @Test fun `open discussion is between returns false when created date is out of closed range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertFalse(check(discussion))
  }

  @Test fun `open discussion is between returns false when created date is out of open range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertFalse(check(discussion))
  }

  @Test fun `open discussion is between returns false when created date is in a closed range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertFalse(check(discussion))
  }

  @Test fun `open discussion is between returns true when created date is in an open range`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = DiscussionIsBetween(rangeStart, rangeEnd)

    assertTrue(check(discussion))
  }

  // endregion Open discussion

}
