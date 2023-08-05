package calculator

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlin.test.Test
import kotlinx.datetime.Instant
import stubs.Stubs
import utils.days
import utils.hours

class CycleTimeCalculatorTest {

  private val now: Instant = Instant.parse("2023-04-05T10:00:00Z")
  private val repos = listOf(Stubs.metrics.cycleTimeRepo1, Stubs.metrics.cycleTimeRepo2)
  private val calculate = { CycleTimeCalculator(now).calculate(repos) }

  @Test fun `calculation is correct per author`() {
    val valuesPerAuthor = calculate().perAuthor

    assertThat(valuesPerAuthor[Stubs.metrics.user1]).isEqualTo(4.days + 1.hours)
    assertThat(valuesPerAuthor[Stubs.metrics.user2]).isEqualTo(6.days)
    assertThat(valuesPerAuthor[Stubs.metrics.user3]).isEqualTo(2.hours)
    assertThat(valuesPerAuthor[Stubs.metrics.user4]).isEqualTo(4.hours)
  }

  @Test fun `calculation is correct per reviewer`() {
    val valuesPerReviewer = calculate().perReviewer

    assertThat(valuesPerReviewer[Stubs.metrics.user1]).isNull()
    assertThat(valuesPerReviewer[Stubs.metrics.user2]).isEqualTo(1.days)
    assertThat(valuesPerReviewer[Stubs.metrics.user3]).isEqualTo(9.days + 5.hours)
    assertThat(valuesPerReviewer[Stubs.metrics.user4]).isEqualTo(3.days)
  }

  @Test fun `calculation is correct per code review`() {
    val valuesPerCodeReview = calculate().perCodeReview

    assertThat(valuesPerCodeReview[repos[0].codeReviews[0]]).isEqualTo(1.days)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[1]]).isEqualTo(2.days)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[2]]).isEqualTo(3.days)
    assertThat(valuesPerCodeReview[repos[0].codeReviews[3]]).isEqualTo(4.days)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[0]]).isEqualTo(1.hours)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[1]]).isEqualTo(2.hours)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[2]]).isEqualTo(3.hours)
    assertThat(valuesPerCodeReview[repos[1].codeReviews[3]]).isEqualTo(1.hours)
  }

  @Test fun `calculation is correct per discussion`() {
    val valuesPerDiscussion = calculate().perDiscussion

    assertThat(valuesPerDiscussion).isEmpty()
  }

  @Test fun `calculation is correct per repository`() {
    val valuesPerRepository = calculate().perRepository

    assertThat(valuesPerRepository[repos[0]]).isEqualTo(10.days)
    assertThat(valuesPerRepository[repos[1]]).isEqualTo(7.hours)
  }

}
