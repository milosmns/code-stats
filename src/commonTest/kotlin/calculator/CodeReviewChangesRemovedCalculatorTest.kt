package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import stubs.Stubs

class CodeReviewChangesRemovedCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewChangeRepo1, Stubs.metrics.codeReviewChangeRepo2)
  private val calculate = { CodeReviewChangesRemovedCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(31)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(0)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(10)
    assertThat(valuesPerAuthor[Stubs.metrics.user4]).isNull()
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(10)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(30)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(10)
    assertThat(valuesPerReviewer[Stubs.metrics.user4]).isEqualTo(10)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(20)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(1)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[3]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[3]]).isEqualTo(0)
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
