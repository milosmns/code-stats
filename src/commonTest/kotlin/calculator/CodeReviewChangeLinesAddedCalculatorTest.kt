package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import stubs.Stubs

class CodeReviewChangeLinesAddedCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewLineRepo1, Stubs.metrics.codeReviewLineRepo2)
  private val calculate = { CodeReviewChangeLinesAddedCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(35)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(6)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isNull()
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(6)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(30)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(11)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(30)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(1)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(5)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(5)
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(31)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(10)
  }

}
