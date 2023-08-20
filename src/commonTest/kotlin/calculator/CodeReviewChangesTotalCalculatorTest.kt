package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import stubs.Stubs

class CodeReviewChangesTotalCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewChangeRepo1, Stubs.metrics.codeReviewChangeRepo2)
  private val calculate = { CodeReviewChangesTotalCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(76)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(12)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(25)
    assertThat(valuesPerAuthor[Stubs.metrics.user4]).isNull()
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(30)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(65)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(30)
    assertThat(valuesPerReviewer[Stubs.metrics.user4]).isEqualTo(37)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(45)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(1)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[3]]).isEqualTo(20)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(5)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(30)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(1)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[3]]).isEqualTo(1)
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(76)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(37)
  }

}
