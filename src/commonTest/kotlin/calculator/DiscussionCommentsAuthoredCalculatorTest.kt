package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import kotlin.test.Test
import stubs.Stubs

class DiscussionCommentsAuthoredCalculatorTest {

  private val repos = listOf(Stubs.metrics.discussionCommentsRepo1, Stubs.metrics.discussionCommentsRepo2)
  private val calculate = { DiscussionCommentsAuthoredCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(7)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(4)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(13)
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(4)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(1)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(8)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview).isEmpty()
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion[repos[0].discussions[0]]).isEqualTo(7)
    assertThat(valuesPerDiscussion[repos[0].discussions[1]]).isEqualTo(1)
    assertThat(valuesPerDiscussion[repos[1].discussions[0]]).isEqualTo(7)
    assertThat(valuesPerDiscussion[repos[1].discussions[1]]).isEqualTo(5)
    assertThat(valuesPerDiscussion[repos[1].discussions[2]]).isEqualTo(4)
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(8)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(16)
  }

}
