package history.filter.transform

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class RepositoryCreatedAtTransformTest {

  // region Code Review components
  private val earlyCodeReviewComment = Stubs.generic.codeReviewComment.copy(
    createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
  )
  private val validCodeReviewComment = Stubs.generic.codeReviewComment.copy(
    createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
  )
  private val lateCodeReviewComment = Stubs.generic.codeReviewComment.copy(
    createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
  )
  private val codeReviewComments = listOf(earlyCodeReviewComment, validCodeReviewComment, lateCodeReviewComment)

  private val earlyCodeReviewFeedback = Stubs.generic.codeReviewFeedback.copy(
    submittedAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
  )
  private val validCodeReviewFeedback = Stubs.generic.codeReviewFeedback.copy(
    submittedAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
  )
  private val lateCodeReviewFeedback = Stubs.generic.codeReviewFeedback.copy(
    submittedAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
  )
  private val codeReviewFeedbacks = listOf(earlyCodeReviewFeedback, validCodeReviewFeedback, lateCodeReviewFeedback)
  // endregion Code Review components

  // region Discussion components
  private val earlyDiscussionComment = Stubs.generic.discussionComment.copy(
    createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
  )
  private val validDiscussionComment = Stubs.generic.discussionComment.copy(
    createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
  )
  private val lateDiscussionComment = Stubs.generic.discussionComment.copy(
    createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
  )
  private val discussionComments = listOf(earlyDiscussionComment, validDiscussionComment, lateDiscussionComment)
  // endregion Discussion components

  // region Repository components
  private val earlyCodeReview = Stubs.generic.codeReview.copy(
    createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    mergedAt = LocalDateTime(2023, 2, 28, 10, 45, 20),
    closedAt = LocalDateTime(2023, 2, 28, 10, 50, 20),
    comments = codeReviewComments,
    feedbacks = codeReviewFeedbacks,
  )
  private val validCodeReview = Stubs.generic.codeReview.copy(
    createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
    mergedAt = LocalDateTime(2023, 3, 1, 10, 45, 20),
    closedAt = LocalDateTime(2023, 3, 1, 10, 50, 20),
    comments = codeReviewComments,
    feedbacks = codeReviewFeedbacks,
  )
  private val lateCodeReview = Stubs.generic.codeReview.copy(
    createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
    mergedAt = LocalDateTime(2023, 3, 2, 10, 45, 20),
    closedAt = LocalDateTime(2023, 3, 2, 10, 50, 20),
    comments = codeReviewComments,
    feedbacks = codeReviewFeedbacks,
  )
  val codeReviews = listOf(earlyCodeReview, validCodeReview, lateCodeReview)

  private val earlyDiscussion = Stubs.generic.discussion.copy(
    createdAt = LocalDateTime(2023, 2, 28, 10, 40, 20),
    closedAt = LocalDateTime(2023, 2, 28, 10, 45, 20),
    comments = discussionComments,
  )
  private val validDiscussion = Stubs.generic.discussion.copy(
    createdAt = LocalDateTime(2023, 3, 1, 10, 40, 20),
    closedAt = LocalDateTime(2023, 3, 1, 10, 45, 20),
    comments = discussionComments,
  )
  private val lateDiscussion = Stubs.generic.discussion.copy(
    createdAt = LocalDateTime(2023, 3, 2, 10, 40, 20),
    closedAt = LocalDateTime(2023, 3, 2, 10, 45, 20),
    comments = discussionComments,
  )
  private val discussions = listOf(earlyDiscussion, validDiscussion, lateDiscussion)
  // endregion Repository components

  val repository = Stubs.generic.repository.copy(
    codeReviews = codeReviews,
    discussions = discussions,
  )

  @Test fun `transform is applied correctly - apply to all`() {
    val expectedCodeReviewComments = listOf(validCodeReviewComment)
    val expectedCodeReviewFeedbacks = listOf(validCodeReviewFeedback)
    val expectedCodeReviews = listOf(
      validCodeReview.copy(
        comments = expectedCodeReviewComments,
        feedbacks = expectedCodeReviewFeedbacks,
      ),
    )

    val expectedDiscussionComments = listOf(validDiscussionComment)
    val expectedDiscussions = listOf(
      validDiscussion.copy(
        comments = expectedDiscussionComments,
      ),
    )

    val expectedRepository = Stubs.generic.repository.copy(
      codeReviews = expectedCodeReviews,
      discussions = expectedDiscussions,
    )

    val transform = RepositoryCreatedAtTransform(
      createdAt = LocalDate(2023, 3, 1),
      applyToCommentsAndFeedbacks = true,
      applyToCodeReviewsAndDiscussions = true,
    )

    assertThat(transform(repository)).isEqualTo(expectedRepository)
  }

  @Test fun `transform is applied correctly - apply to code reviews and discussions only`() {
    val expectedCodeReviewComments = codeReviewComments
    val expectedCodeReviewFeedbacks = codeReviewFeedbacks
    val expectedCodeReviews = listOf(
      validCodeReview.copy(
        comments = expectedCodeReviewComments,
        feedbacks = expectedCodeReviewFeedbacks,
      ),
    )

    val expectedDiscussionComments = discussionComments
    val expectedDiscussions = listOf(
      validDiscussion.copy(
        comments = expectedDiscussionComments,
      ),
    )

    val expectedRepository = Stubs.generic.repository.copy(
      codeReviews = expectedCodeReviews,
      discussions = expectedDiscussions,
    )

    val transform = RepositoryCreatedAtTransform(
      createdAt = LocalDate(2023, 3, 1),
      applyToCommentsAndFeedbacks = false,
      applyToCodeReviewsAndDiscussions = true,
    )

    assertThat(transform(repository)).isEqualTo(expectedRepository)
  }

  @Test fun `transform is applied correctly - apply to comments and feedbacks only`() {
    val expectedCodeReviewComments = listOf(validCodeReviewComment)
    val expectedCodeReviewFeedbacks = listOf(validCodeReviewFeedback)
    val expectedCodeReviews = codeReviews.map {
      it.copy(
        comments = expectedCodeReviewComments,
        feedbacks = expectedCodeReviewFeedbacks,
      )
    }

    val expectedDiscussionComments = listOf(validDiscussionComment)
    val expectedDiscussions = discussions.map {
      it.copy(
        comments = expectedDiscussionComments,
      )
    }

    val expectedRepository = Stubs.generic.repository.copy(
      codeReviews = expectedCodeReviews,
      discussions = expectedDiscussions,
    )

    val transform = RepositoryCreatedAtTransform(
      createdAt = LocalDate(2023, 3, 1),
      applyToCommentsAndFeedbacks = true,
      applyToCodeReviewsAndDiscussions = false,
    )

    assertThat(transform(repository)).isEqualTo(expectedRepository)
  }

  @Test fun `transform is applied correctly - apply to none`() {
    val transform = RepositoryCreatedAtTransform(
      createdAt = LocalDate(2023, 3, 1),
      applyToCommentsAndFeedbacks = false,
      applyToCodeReviewsAndDiscussions = false,
    )

    assertThat(transform(repository)).isEqualTo(repository)
  }

}
