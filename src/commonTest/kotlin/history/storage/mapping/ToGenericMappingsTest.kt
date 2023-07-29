package history.storage.mapping

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import stubs.Stubs

class ToGenericMappingsTest {

  @Test fun `database repository to repository`() {
    val result = Stubs.db.repository.toGeneric()
    val expected = Stubs.generic.repositoryEmpty

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database user to user`() {
    val result = Stubs.db.user.toGeneric()
    val expected = Stubs.generic.user

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database discussion to discussion`() {
    val result = Stubs.db.discussion.toGeneric()
    val expected = Stubs.generic.discussionEmpty

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database discussion comment to discussion comment`() {
    val result = Stubs.db.discussionComment.toGeneric()
    val expected = Stubs.generic.discussionComment

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review to code review`() {
    val result = Stubs.db.codeReview.toGeneric()
    val expected = Stubs.generic.codeReviewEmpty

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review comment to code review comment`() {
    val result = Stubs.db.codeReviewComment.toGeneric()
    val expected = Stubs.generic.codeReviewComment

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review change to code review change`() {
    val result = Stubs.db.codeReviewChange.toGeneric()
    val expected = Stubs.generic.codeReviewChange

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `database code review feedback to code review feedback`() {
    val result = Stubs.db.codeReviewFeedback.toGeneric()
    val expected = Stubs.generic.codeReviewFeedback

    assertThat(result).isEqualTo(expected)
  }

}
