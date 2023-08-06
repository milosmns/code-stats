package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import stubs.Stubs

class CodeReviewChangesModifiedCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewChangeRepo1, Stubs.metrics.codeReviewChangeRepo2)
  private val calculate = { CodeReviewChangesModifiedCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(25)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(6)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(5)
    assertThat(valuesPerAuthor[Stubs.metrics.user4]).isNull()
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(5)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(15)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(10)
    assertThat(valuesPerReviewer[Stubs.metrics.user4]).isEqualTo(16)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(15)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(5)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[3]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(5)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(1)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[3]]).isEqualTo(0)
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(20)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(16)
  }

}
