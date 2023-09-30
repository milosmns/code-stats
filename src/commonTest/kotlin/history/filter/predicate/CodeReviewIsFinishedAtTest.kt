package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CodeReviewIsFinishedAtTest {

  @Test fun `code review is finished at returns false when days mismatch`() {
    val codeReview = Stubs.generic.codeReview.copy(
      closedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewIsFinishedAt(LocalDate(2023, 2, 27))

    assertFalse(check(codeReview))
  }

  @Test fun `code review is finished at returns true when closed days match`() {
    val codeReview = Stubs.generic.codeReview.copy(
      closedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
    )

    val check = CodeReviewIsFinishedAt(LocalDate(2023, 2, 28))

    assertTrue(check(codeReview))
  }

  @Test fun `code review is finished at returns true when merged days match`() {
    val codeReview = Stubs.generic.codeReview.copy(
      closedAt = LocalDateTime(2023, 2, 27, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewIsFinishedAt(LocalDate(2023, 2, 28))

    assertTrue(check(codeReview))
  }

  @Test fun `code review is finished at returns true when both days match`() {
    val codeReview = Stubs.generic.codeReview.copy(
      closedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewIsFinishedAt(LocalDate(2023, 2, 28))

    assertTrue(check(codeReview))
  }

}
