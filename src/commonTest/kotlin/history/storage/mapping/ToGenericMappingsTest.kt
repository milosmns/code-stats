package history.storage.mapping

import codestats.CodeReview as DatabaseCodeReview
import codestats.CodeReviewChange as DatabaseCodeReviewChange
import codestats.CodeReviewComment as DatabaseCodeReviewComment
import codestats.CodeReviewFeedback as DatabaseCodeReviewFeedback
import codestats.Discussion as DatabaseDiscussion
import codestats.DiscussionComment as DatabaseDiscussionComment
import codestats.Repository as DatabaseRepository
import codestats.User as DatabaseUser
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinx.datetime.LocalDateTime
import components.data.CodeReview
import components.data.Discussion
import components.data.Repository
import components.data.User

class ToGenericMappingsTest {

  @Test fun `database repository to repository`() {
    val result = dbRepository().toGeneric()
    val expected = genericEmptyRepository()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database user to user`() {
    val result = dbUser().toGeneric()
    val expected = genericUser()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database discussion to discussion`() {
    val result = dbDiscussion().toGeneric()
    val expected = genericEmptyDiscussion()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database discussion comment to discussion comment`() {
    val result = dbDiscussionComment().toGeneric()
    val expected = genericDiscussionComment()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review to code review`() {
    val result = dbCodeReview().toGeneric()
    val expected = genericEmptyCodeReview()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review comment to code review comment`() {
    val result = dbCodeReviewComment().toGeneric()
    val expected = genericCodeReviewComment()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review change to code review change`() {
    val result = dbCodeReviewChange().toGeneric()
    val expected = genericCodeReviewChange()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review feedback to code review feedback`() {
    val result = dbCodeReviewFeedback().toGeneric()
    val expected = genericCodeReviewFeedback()

    assertThat(result).isEqualTo(expected)
  }

  // region Helpers

  private val fixedDateTime = LocalDateTime(2023, 6, 1, 15, 45, 0, 0)
  private val fixedDateTimeString = "2023-06-01T15:45"
  private val fixedRepoOwner = "Repository owner"
  private val fixedRepoName = "Repository name"

  private fun genericEmptyRepository() = Repository(fixedRepoOwner, fixedRepoName, emptyList(), emptyList())

  private fun genericUser() = User(
    login = "octocat",
  )

  private fun genericEmptyDiscussion() = Discussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = genericUser(),
    createdAt = fixedDateTime,
    closedAt = fixedDateTime,
    comments = emptyList(),
  )

  private fun genericDiscussionComment() = Discussion.Comment(
    id = "1234",
    body = "Comment body",
    createdAt = fixedDateTime,
    author = genericUser(),
  )

  private fun genericEmptyCodeReview() = CodeReview(
    id = 1235,
    number = 1,
    state = CodeReview.State.OPEN,
    title = "Pull request title",
    body = "Pull request body",
    author = genericUser(),
    requestedReviewers = listOf(genericUser()),
    isDraft = true,
    changedFiles = 3,
    comments = emptyList(),
    changes = emptyList(),
    feedbacks = emptyList(),
    createdAt = fixedDateTime,
    closedAt = fixedDateTime,
    mergedAt = fixedDateTime,
  )

  private fun genericCodeReviewComment() = CodeReview.Comment(
    id = 1236,
    body = "Comment body",
    createdAt = fixedDateTime,
    author = genericUser(),
  )

  private fun genericCodeReviewFeedback() = CodeReview.Feedback(
    id = 1237,
    body = "Review body",
    state = CodeReview.Feedback.State.APPROVED,
    submittedAt = fixedDateTime,
    author = genericUser(),
  )

  private fun genericCodeReviewChange() = CodeReview.Change(
    status = CodeReview.Change.Status.ADDED,
    additions = 1,
    deletions = 2,
    total = 3,
    fileName = "file.txt",
  )

  private fun dbRepository() = DatabaseRepository(
    owner = fixedRepoOwner,
    name = fixedRepoName,
  )

  private fun dbUser() = DatabaseUser(
    login = "octocat",
  )

  private fun dbDiscussion() = DatabaseDiscussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author_login = dbUser().login,
    created_at = fixedDateTimeString,
    closed_at = fixedDateTimeString,
    repo_owner = fixedRepoOwner,
    repo_name = fixedRepoName,
  )

  private fun dbDiscussionComment() = DatabaseDiscussionComment(
    id = "1234",
    body = "Comment body",
    created_at = fixedDateTimeString,
    discussion_id = dbDiscussion().id,
    author_login = dbUser().login,
    repo_owner = fixedRepoOwner,
    repo_name = fixedRepoName,
  )

  private fun dbCodeReview() = DatabaseCodeReview(
    id = 1235,
    number = 1,
    state = CodeReview.State.OPEN.name,
    title = "Pull request title",
    body = "Pull request body",
    author_login = dbUser().login,
    reviewers_csv = dbUser().login,
    is_draft = 1,
    changed_files = 3,
    created_at = fixedDateTimeString,
    closed_at = fixedDateTimeString,
    merged_at = fixedDateTimeString,
    repo_owner = fixedRepoOwner,
    repo_name = fixedRepoName,
  )

  private fun dbCodeReviewComment() = DatabaseCodeReviewComment(
    id = 1236,
    body = "Comment body",
    created_at = fixedDateTimeString,
    code_review_id = dbCodeReview().id,
    author_login = dbUser().login,
    repo_owner = fixedRepoOwner,
    repo_name = fixedRepoName,
  )

  private fun dbCodeReviewFeedback() = DatabaseCodeReviewFeedback(
    id = 1237,
    body = "Review body",
    state = CodeReview.Feedback.State.APPROVED.name,
    submitted_at = fixedDateTimeString,
    code_review_id = dbCodeReview().id,
    author_login = dbUser().login,
    repo_owner = fixedRepoOwner,
    repo_name = fixedRepoName,
  )

  private fun dbCodeReviewChange() = DatabaseCodeReviewChange(
    status = CodeReview.Change.Status.ADDED.name,
    additions = 1,
    deletions = 2,
    total = 3,
    file_name = "file.txt",
    code_review_id = dbCodeReview().id,
    repo_owner = fixedRepoOwner,
    repo_name = fixedRepoName,
  )

  // endregion Helpers

}
