package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import stubs.Stubs

class CodeReviewsCalculatorTest {

  private val repos = listOf(Stubs.metrics.codeReviewsTotalsRepo1, Stubs.metrics.codeReviewsTotalsRepo2)
  private val calculate = { CodeReviewsCalculator().calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(1)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(2)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(1)
    assertThat(valuesPerAuthor[Stubs.metrics.user4]).isEqualTo(2)
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isEqualTo(3)
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(2)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(1)
    assertThat(valuesPerReviewer[Stubs.metrics.user4]).isNull()
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview).isEmpty()
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(4)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(2)
  }

}
