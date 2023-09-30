package history.filter.transform

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class RepositoryFinishedAtTransformTest {

  @Test fun `transform is applied correctly`() {
    // Code Review components
    val earlyCodeReviewComment = Stubs.generic.codeReviewComment.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )
    val validCodeReviewComment = Stubs.generic.codeReviewComment.copy(
      createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
    )
    val lateCodeReviewComment = Stubs.generic.codeReviewComment.copy(
      createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
    )
    val codeReviewComments = listOf(earlyCodeReviewComment, validCodeReviewComment, lateCodeReviewComment)

    val earlyCodeReviewFeedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )
    val validCodeReviewFeedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
    )
    val lateCodeReviewFeedback = Stubs.generic.codeReviewFeedback.copy(
      submittedAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
    )
    val codeReviewFeedbacks = listOf(earlyCodeReviewFeedback, validCodeReviewFeedback, lateCodeReviewFeedback)

    // Discussion components
    val earlyDiscussionComment = Stubs.generic.discussionComment.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    )
    val validDiscussionComment = Stubs.generic.discussionComment.copy(
      createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
    )
    val lateDiscussionComment = Stubs.generic.discussionComment.copy(
      createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
    )
    val discussionComments = listOf(earlyDiscussionComment, validDiscussionComment, lateDiscussionComment)

    // Repository components
    val earlyCodeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 2, 28, 10, 45, 20),
      closedAt = LocalDateTime(2023, 2, 28, 10, 50, 20),
      comments = codeReviewComments,
      feedbacks = codeReviewFeedbacks,
    )
    val validCodeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 3, 1, 10, 45, 20),
      closedAt = LocalDateTime(2023, 3, 1, 10, 50, 20),
      comments = codeReviewComments,
      feedbacks = codeReviewFeedbacks,
    )
    val lateCodeReview = Stubs.generic.codeReview.copy(
      createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
      mergedAt = LocalDateTime(2023, 3, 2, 10, 45, 20),
      closedAt = LocalDateTime(2023, 3, 2, 10, 50, 20),
      comments = codeReviewComments,
      feedbacks = codeReviewFeedbacks,
    )
    val codeReviews = listOf(earlyCodeReview, validCodeReview, lateCodeReview)

    val earlyDiscussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
      closedAt = LocalDateTime(2023, 2, 28, 10, 45, 20),
      comments = discussionComments,
    )
    val validDiscussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
      closedAt = LocalDateTime(2023, 3, 1, 10, 45, 20),
      comments = discussionComments,
    )
    val lateDiscussion = Stubs.generic.discussion.copy(
      createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
      closedAt = LocalDateTime(2023, 3, 2, 10, 45, 20),
      comments = discussionComments,
    )
    val discussions = listOf(earlyDiscussion, validDiscussion, lateDiscussion)

    // Evaluation
    val repository = Stubs.generic.repository.copy(
      codeReviews = codeReviews,
      discussions = discussions,
    )

    val expectedCodeReviews = listOf(validCodeReview)
    val expectedDiscussions = listOf(validDiscussion)
    val expectedRepository = Stubs.generic.repository.copy(
      codeReviews = expectedCodeReviews,
      discussions = expectedDiscussions,
    )

    val transform = RepositoryFinishedAtTransform(
      finishedAt = LocalDate(2023, 3, 1),
    )

    assertThat(transform(repository)).isEqualTo(expectedRepository)
  }

}
