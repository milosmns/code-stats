package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import stubs.Stubs
import kotlin.test.Test

class DiscussionCommentsReceivedCalculatorTest {

  private val repos = listOf(Stubs.metrics.discussionCommentsRepo1, Stubs.metrics.discussionCommentsRepo2)
  private val calculate = { DiscussionCommentsReceivedCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(4)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(8)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(1)
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isNull()
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isNull()
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isNull()
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview).isEmpty()
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion[repos[0].discussions[0]]).isNull()
    assertThat(valuesPerDiscussion[repos[0].discussions[1]]).isNull()
    assertThat(valuesPerDiscussion[repos[1].discussions[0]]).isNull()
    assertThat(valuesPerDiscussion[repos[1].discussions[1]]).isNull()
    assertThat(valuesPerDiscussion[repos[1].discussions[2]]).isNull()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isNull()
    assertThat(valuesPerRepository[repos[1]]).isNull()
  }

}
