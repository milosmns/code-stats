package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CodeReviewFeedbackIsSubmittedAtTest {

  @Test fun `code review feedback is submitted at returns false when days mismatch`() {
    val feedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewFeedbackIsSubmittedAt(LocalDate(2023, 2, 27))

    assertFalse(check(feedback))
  }

  @Test fun `code review feedback is submitted at returns true when days match`() {
    val feedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewFeedbackIsSubmittedAt(LocalDate(2023, 2, 28))

    assertTrue(check(feedback))
  }

}
