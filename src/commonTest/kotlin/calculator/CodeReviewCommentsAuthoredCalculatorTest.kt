package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import kotlin.test.Test
import stubs.Stubs

class CodeReviewCommentsAuthoredCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewCommentRepo1, Stubs.metrics.codeReviewCommentRepo2)
  private val calculate = { CodeReviewCommentsAuthoredCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(7)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(7)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(5)
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(0)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(2)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(0)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(4)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(3)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(3)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(2)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(4)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(3)
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(10)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(9)
  }

}
