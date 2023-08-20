package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import stubs.Stubs

class CodeReviewChangeLinesTotalCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewLineRepo1, Stubs.metrics.codeReviewLineRepo2)
  private val calculate = { CodeReviewChangeLinesTotalCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(51)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(21)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isNull()
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(11)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(40)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(31)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(40)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(1)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(1)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(10)
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(42)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(30)
  }

}
