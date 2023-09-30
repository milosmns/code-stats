package history.filter.transform

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CodeReviewCreatedAtTransformTest {

  @Test fun `transform is applied correctly`() {
    val earlyComment = Stubs.generic.codeReviewComment.copy(createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20))
    val validComment = Stubs.generic.codeReviewComment.copy(createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20))
    val lateComment = Stubs.generic.codeReviewComment.copy(createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20))
    val comments = listOf(earlyComment, validComment, lateComment)

    val earlyFeedback = Stubs.generic.codeReviewFeedback.copy(submittedAt = LocalDateTime(2023, 2, 28, 10, 40, 20))
    val validFeedback = Stubs.generic.codeReviewFeedback.copy(submittedAt = LocalDateTime(2023, 3, 1, 10, 40, 20))
    val lateFeedback = Stubs.generic.codeReviewFeedback.copy(submittedAt = LocalDateTime(2023, 3, 2, 10, 40, 20))
    val feedbacks = listOf(earlyFeedback, validFeedback, lateFeedback)

    val codeReview = Stubs.generic.codeReview.copy(comments = comments, feedbacks = feedbacks)
    val expected = Stubs.generic.codeReview.copy(comments = listOf(validComment), feedbacks = listOf(validFeedback))
    val transform = CodeReviewCreatedAtTransform(
      createdAt = LocalDate(2023, 3, 1),
    )

    assertThat(transform(codeReview)).isEqualTo(expected)
  }

}
