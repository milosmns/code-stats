package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CodeReviewCommentIsBetweenTest {

  @Test fun `code review comment is between returns true when creation date is in range`() {
    val comment = Stubs.generic.codeReviewComment.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewCommentIsBetween(rangeStart, rangeEnd)

    assertTrue(check(comment))
  }

  @Test fun `code review comment is between returns false when creation date is outside of range`() {
    val comment = Stubs.generic.codeReviewComment.copy(
      createdAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 28)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewCommentIsBetween(rangeStart, rangeEnd)

    assertFalse(check(comment))
  }

}
