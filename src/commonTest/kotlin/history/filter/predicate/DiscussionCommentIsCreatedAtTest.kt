package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class DiscussionCommentIsCreatedAtTest {

  @Test fun `discussion comment is created at returns false when days mismatch`() {
    val comment = Stubs.generic.discussionComment.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = DiscussionCommentIsCreatedAt(LocalDate(2023, 2, 27))

    assertFalse(check(comment))
  }

  @Test fun `discussion comment is created at returns true when days match`() {
    val comment = Stubs.generic.discussionComment.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = DiscussionCommentIsCreatedAt(LocalDate(2023, 2, 28))

    assertTrue(check(comment))
  }

}
