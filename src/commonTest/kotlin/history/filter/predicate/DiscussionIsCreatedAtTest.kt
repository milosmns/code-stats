package history.filter.predicate

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class DiscussionIsCreatedAtTest {

  @Test fun `discussion is created at returns false when days mismatch`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = DiscussionIsCreatedAt(LocalDate(2023, 2, 27))

    assertFalse(check(discussion))
  }

  @Test fun `discussion is created at returns true when days match`() {
    val discussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )

    val check = DiscussionIsCreatedAt(LocalDate(2023, 2, 28))

    assertTrue(check(discussion))
  }

}
