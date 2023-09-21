package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CodeReviewFeedbackIsBetweenTest {

  @Test fun `code review feedback is between returns true when submitted date is in range`() {
    val feedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 27)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewFeedbackIsBetween(rangeStart, rangeEnd)

    assertTrue(check(feedback))
  }

  @Test fun `code review feedback is between returns false when submitted date is outside of range`() {
    val feedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
    )
    val rangeStart = LocalDate(2023, 2, 28)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewFeedbackIsBetween(rangeStart, rangeEnd)

    assertFalse(check(feedback))
  }

  @Test fun `code review feedback is between returns false when submitted date is null`() {
    val feedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = null,
    )
    val rangeStart = LocalDate(2023, 2, 28)
    val rangeEnd = LocalDate(2023, 3, 1)

    val check = CodeReviewFeedbackIsBetween(rangeStart, rangeEnd)

    assertFalse(check(feedback))
  }

}
