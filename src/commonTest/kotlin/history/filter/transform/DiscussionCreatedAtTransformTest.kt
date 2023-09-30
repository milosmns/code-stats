package history.filter.transform

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class DiscussionCreatedAtTransformTest {

  @Test fun `transform is applied correctly`() {
    val earlyComment = Stubs.generic.discussionComment.copy(createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20))
    val validComment = Stubs.generic.discussionComment.copy(createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20))
    val lateComment = Stubs.generic.discussionComment.copy(createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20))
    val comments = listOf(earlyComment, validComment, lateComment)

    val discussion = Stubs.generic.discussion.copy(comments = comments)
    val expected = Stubs.generic.discussion.copy(comments = listOf(validComment))
    val transform = DiscussionCreatedAtTransform(
      createdAt = LocalDate(2023, 3, 1),
    )

    assertThat(transform(discussion)).isEqualTo(expected)
  }

}
