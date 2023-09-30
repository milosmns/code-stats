package history.filter.predicate

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CodeReviewCommentIsCreatedAtTest {

  @Test fun `code review comment is created at returns false when days mismatch`() {
    val comment = Stubs.generic.codeReviewComment.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewCommentIsCreatedAt(LocalDate(2023, 2, 27))

    assertFalse(check(comment))
  }

  @Test fun `code review comment is created at returns true when days match`() {
    val comment = Stubs.generic.codeReviewComment.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = CodeReviewCommentIsCreatedAt(LocalDate(2023, 2, 28))

    assertTrue(check(comment))
  }

}
