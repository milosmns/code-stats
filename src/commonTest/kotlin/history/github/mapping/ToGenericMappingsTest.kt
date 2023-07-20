package history.github.mapping

import assertk.assertThat
import assertk.assertions.isEqualTo
import history.github.models.GitHubDiscussion
import history.github.models.GitHubPullRequest
import history.github.models.GitHubPullRequest.File
import history.github.models.GitHubPullRequest.Review
import history.github.models.GitHubRepository
import history.github.models.GitHubUser
import kotlin.test.Test
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import models.CodeReview
import models.CodeReview.Change
import models.CodeReview.Feedback
import models.Discussion
import models.User

class ToGenericMappingsTest {

  private val fixedDateTime = LocalDateTime(2023, 6, 1, 15, 45, 0, 0)

  @Test fun `GitHub user to generic user`() {
    val result = gitHubUser().toGeneric()
    val expected = genericUser()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub discussion comment to generic comment`() {
    val result = gitHubDiscussionComment().toGeneric()
    val expected = genericDiscussionComment()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub discussion to generic discussion`() {
    val result = gitHubDiscussion().toGeneric()
    val expected = genericDiscussion()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub pull request state to generic code review state`() {
    val results = GitHubPullRequest.State.values().map(GitHubPullRequest.State::toGeneric)
    val expected = CodeReview.State.values().toList()

    assertThat(results).isEqualTo(expected)
  }

  @Test fun `GitHub pull request file status to generic change status`() {
    val results = File.Status.values().map(File.Status::toGeneric)
    val expected = Change.Status.values().toList()

    assertThat(results).isEqualTo(expected)
  }

  @Test fun `GitHub pull request file to generic change`() {
    val result = gitHubPullRequestFile().toGeneric()
    val expected = genericCodeReviewChange()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub review state to generic feedback state`() {
    val results = Review.State.values().map(Review.State::toGeneric)
    val expected = Feedback.State.values().toList()

    assertThat(results)
      .isEqualTo(expected)
  }

  @Test fun `GitHub pull request review to generic feedback`() {
    val result = gitHubPullRequestReview().toGeneric()
    val expected = genericCodeReviewFeedback()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub pull request comment to generic comment`() {
    val result = gitHubPullRequestComment().toGeneric()
    val expected = genericCodeReviewComment()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub pull request to generic code review`() {
    val result = gitHubPullRequest().toGeneric()
    val expected = genericCodeReview()

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub repository to generic repository`() {
    val result = gitHubRepository().toGeneric()
    val expected = genericRepository()

    assertThat(result).isEqualTo(expected)
  }

  // region Helpers

  private fun genericUser() = User(
    login = "octocat",
  )

  private fun genericRepository() = models.Repository(
    owner = "Repository owner",
    name = "Repository name",
    codeReviews = listOf(genericCodeReview()),
    discussions = listOf(genericDiscussion()),
  )

  private fun genericDiscussion() = Discussion(
    id = "1238",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = genericUser(),
    createdAt = fixedDateTime,
    closedAt = fixedDateTime,
    comments = listOf(genericDiscussionComment()),
  )

  private fun genericDiscussionComment() = Discussion.Comment(
    id = "1237",
    body = "Comment body",
    createdAt = fixedDateTime,
    author = genericUser(),
  )

  private fun genericCodeReview() = CodeReview(
    id = 1235,
    number = 1,
    state = CodeReview.State.OPEN,
    title = "Pull request title",
    body = "Pull request body",
    author = genericUser(),
    requestedReviewers = listOf(genericUser()),
    isDraft = true,
    changedFiles = 3,
    comments = listOf(genericCodeReviewComment()),
    changes = listOf(genericCodeReviewChange()),
    feedbacks = listOf(genericCodeReviewFeedback()),
    createdAt = fixedDateTime,
    closedAt = fixedDateTime,
    mergedAt = fixedDateTime,
  )

  private fun genericCodeReviewComment() = CodeReview.Comment(
    id = 1230,
    body = "Comment body",
    createdAt = fixedDateTime,
    author = genericUser(),
  )

  private fun genericCodeReviewChange() = Change(
    status = Change.Status.ADDED,
    additions = 1,
    deletions = 2,
    total = 3,
    fileName = "file.txt",
  )

  private fun genericCodeReviewFeedback() = Feedback(
    id = 1234,
    body = "Review body",
    state = Feedback.State.APPROVED,
    submittedAt = fixedDateTime,
    author = genericUser(),
  )

  private fun gitHubUser() = GitHubUser(
    login = "octocat",
  )

  private fun gitHubRepository() = GitHubRepository(
    owner = "Repository owner",
    name = "Repository name",
    pullRequests = listOf(gitHubPullRequest()),
    repoDiscussions = listOf(gitHubDiscussion()),
  )

  private fun gitHubDiscussion() = GitHubDiscussion(
    id = "1238",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = gitHubUser(),
    createdAt = fixedDateTime,
    closedAt = fixedDateTime,
    comments = listOf(gitHubDiscussionComment()),
  )

  private fun gitHubDiscussionComment() = GitHubDiscussion.Comment(
    id = "1237",
    body = "Comment body",
    createdAt = fixedDateTime,
    author = gitHubUser(),
  )

  private fun gitHubPullRequest() = GitHubPullRequest(
    id = 1235,
    number = 1,
    state = GitHubPullRequest.State.OPEN,
    title = "Pull request title",
    body = "Pull request body",
    author = gitHubUser(),
    requestedReviewers = listOf(gitHubUser()),
    isDraft = true,
    changedFiles = 3,
    comments = listOf(gitHubPullRequestComment()),
    files = listOf(gitHubPullRequestFile()),
    reviews = listOf(gitHubPullRequestReview()),
    createdAtInstant = fixedDateTime.toInstant(TimeZone.UTC),
    closedAtInstant = fixedDateTime.toInstant(TimeZone.UTC),
    mergedAtInstant = fixedDateTime.toInstant(TimeZone.UTC),
  )

  private fun gitHubPullRequestComment() = GitHubPullRequest.Comment(
    id = 1230,
    body = "Comment body",
    createdAtInstant = fixedDateTime.toInstant(TimeZone.UTC),
    author = gitHubUser(),
  )

  private fun gitHubPullRequestFile() = File(
    status = File.Status.ADDED,
    additions = 1,
    deletions = 2,
    total = 3,
    fileName = "file.txt",
  )

  private fun gitHubPullRequestReview() = Review(
    id = 1234,
    body = "Review body",
    state = Review.State.APPROVED,
    submittedAtInstant = fixedDateTime.toInstant(TimeZone.UTC),
    author = gitHubUser(),
  )

  // endregion Helpers

}
