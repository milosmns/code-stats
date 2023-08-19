package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import stubs.Stubs

class CodeReviewFeedbacksApprovedCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewFeedbackRepo1, Stubs.metrics.codeReviewFeedbackRepo2)
  private val calculate = { CodeReviewFeedbacksApprovedCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(13)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(21)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isNull()
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isNull()
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isNull()
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(34)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(3)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(0)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(10)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(11)
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(13)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(21)
  }

}
