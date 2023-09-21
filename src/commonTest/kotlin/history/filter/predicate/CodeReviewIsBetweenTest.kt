package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CodeReviewIsBetweenTest {

  // region Merged code review

  @Test fun `merged code review is between returns false when created date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `merged code review is between returns false when created date is out of open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `merged code review is between returns false when merged date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 3, 2, 11, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `merged code review is between returns true when created and merged dates are in open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertTrue(check(codeReview))
  }

  @Test fun `merged code review is between returns true when created and merged dates are in closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertTrue(check(codeReview))
  }

  // endregion Merged code review

  // region Closed code review

  @Test fun `closed code review is between returns false when created date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = null,
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `closed code review is between returns false when created date is out of open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = null,
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `closed code review is between returns false when closed date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
      mergedAt = null,
      closedAt = LocalDateTime(2023, 3, 2, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `closed code review is between returns true when created and closed dates are in open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = null,
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertTrue(check(codeReview))
  }

  @Test fun `closed code review is between returns true when created and closed dates are in closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = null,
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertTrue(check(codeReview))
  }

  // endregion Closed code review

  // region Merged and closed code review

  @Test fun `merged and closed code review is between returns false when created date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 21),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `merged and closed code review is between returns false when merged date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 3, 2, 11, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 21),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `merged and closed code review is between returns false when closed date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = LocalDateTime(2023, 3, 2, 11, 40, 21),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `merged and closed code review is between returns false when created date is out of open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 21),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `merged and closed code review is between returns true when all dates are in open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 21),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertTrue(check(codeReview))
  }

  @Test fun `merged and closed code review is between returns true when all dates are in closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 11, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 11, 40, 21),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertTrue(check(codeReview))
  }

  // endregion Merged and closed code review

  // region Open code review

  @Test fun `open code review is between returns false when created date is out of closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = null,
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `open code review is between returns false when created date is out of open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 26, 10, 40, 20),
      mergedAt = null,
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview))
  }

  @Test fun `open code review is between returns true when created date is in open range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = null,
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = null

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertTrue(check(codeReview))
  }

  @Test fun `open code review is between returns false when created date is in closed range`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = null,
      closedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewIsBetween(rangeStart, rangeEnd)

    assertFalse(check(codeReview)) // because it's not closed
  }

  // endregion Open code review

}
