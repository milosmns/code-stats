package history.github.mapping

import assertk.assertThat
import assertk.assertions.isEqualTo
import components.data.CodeReview
import components.data.CodeReview.Change
import components.data.CodeReview.Feedback
import history.github.models.GitHubPullRequest
import history.github.models.GitHubPullRequest.File
import history.github.models.GitHubPullRequest.Review
import kotlin.test.Test
import stubs.Stubs

class ToGenericMappingsTest {

  @Test fun `GitHub user to generic user`() {
    val result = Stubs.gitHub.user.toGeneric()
    val expected = Stubs.generic.user

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub discussion comment to generic comment`() {
    val result = Stubs.gitHub.discussionComment.toGeneric()
    val expected = Stubs.generic.discussionComment

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub discussion to generic discussion`() {
    val result = Stubs.gitHub.discussion.toGeneric()
    val expected = Stubs.generic.discussion

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
    val result = Stubs.gitHub.pullRequestFile.toGeneric()
    val expected = Stubs.generic.codeReviewChange

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub review state to generic feedback state`() {
    val results = Review.State.values().map(Review.State::toGeneric)
    val expected = Feedback.State.values().toList()

    assertThat(results)
      .isEqualTo(expected)
  }

  @Test fun `GitHub pull request review to generic feedback`() {
    val result = Stubs.gitHub.pullRequestReview.toGeneric()
    val expected = Stubs.generic.codeReviewFeedback

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub pull request comment to generic comment`() {
    val result = Stubs.gitHub.pullRequestComment.toGeneric()
    val expected = Stubs.generic.codeReviewComment

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub pull request to generic code review`() {
    val result = Stubs.gitHub.pullRequest.toGeneric()
    val expected = Stubs.generic.codeReview

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GitHub repository to generic repository`() {
    val result = Stubs.gitHub.repository.toGeneric()
    val expected = Stubs.generic.repository

    assertThat(result).isEqualTo(expected)
  }

}
