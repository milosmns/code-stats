package history.storage.mapping

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import stubs.Stubs

class ToStorageMappingsTest {

  @Test fun `repository to database repository`() {
    val result = Stubs.generic.repository.toStorage()
    val expected = Stubs.db.repository

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `user to database user`() {
    val result = Stubs.generic.user.toStorage()
    val expected = Stubs.db.user

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `discussion to database discussion`() {
    val repoOwner = Stubs.generic.repository.owner
    val repoName = Stubs.generic.repository.name
    val result = Stubs.generic.discussion.toStorage(repoOwner, repoName)
    val expected = Stubs.db.discussion

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `discussion comment to database discussion comment`() {
    val repoOwner = Stubs.generic.repository.owner
    val repoName = Stubs.generic.repository.name
    val parentId = Stubs.generic.discussion.id
    val result = Stubs.generic.discussionComment.toStorage(repoOwner, repoName, parentId)
    val expected = Stubs.db.discussionComment

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `code review to database code review`() {
    val repoOwner = Stubs.generic.repository.owner
    val repoName = Stubs.generic.repository.name
    val result = Stubs.generic.codeReview.toStorage(repoOwner, repoName)
    val expected = Stubs.db.codeReview

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `code review comment to database code review comment`() {
    val repoOwner = Stubs.generic.repository.owner
    val repoName = Stubs.generic.repository.name
    val parentId = Stubs.generic.codeReview.id
    val result = Stubs.generic.codeReviewComment.toStorage(repoOwner, repoName, parentId)
    val expected = Stubs.db.codeReviewComment

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `code review change to database code review change`() {
    val repoOwner = Stubs.generic.repository.owner
    val repoName = Stubs.generic.repository.name
    val parentId = Stubs.generic.codeReview.id
    val result = Stubs.generic.codeReviewChange.toStorage(repoOwner, repoName, parentId)
    val expected = Stubs.db.codeReviewChange

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `code review feedback to database code review feedback`() {
    val repoOwner = Stubs.generic.repository.owner
    val repoName = Stubs.generic.repository.name
    val parentId = Stubs.generic.codeReview.id
    val result = Stubs.generic.codeReviewFeedback.toStorage(repoOwner, repoName, parentId)
    val expected = Stubs.db.codeReviewFeedback

    assertThat(result).isEqualTo(expected)
  }

}
