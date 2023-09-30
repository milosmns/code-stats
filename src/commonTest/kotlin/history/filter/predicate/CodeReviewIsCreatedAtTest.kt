package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CodeReviewIsCreatedAtTest {

  @Test fun `code review is created at returns false when days mismatch`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewIsCreatedAt(LocalDate(2023, 2, 27))

    assertFalse(check(codeReview))
  }

  @Test fun `code review is created at returns true when days match`() {
    val codeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewIsCreatedAt(LocalDate(2023, 2, 28))

    assertTrue(check(codeReview))
  }

}
