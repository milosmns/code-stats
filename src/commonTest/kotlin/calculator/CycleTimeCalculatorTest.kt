package calculator

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import components.data.Repository
import components.data.User
import kotlin.test.Test
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import stubs.Stubs

class CycleTimeCalculatorTest {

  @Test fun `cycle time calculation is correct per author`() {
    val cycleTimePerAuthor = calculate().perAuthor

    assertThat(cycleTimePerAuthor[slowUser1]).isEqualTo(4.days + 1.hours)
    assertThat(cycleTimePerAuthor[slowUser2]).isEqualTo(6.days)
    assertThat(cycleTimePerAuthor[fastUser1]).isEqualTo(4.hours)
    assertThat(cycleTimePerAuthor[fastUser2]).isEqualTo(2.hours)
  }

  @Test fun `cycle time calculation is correct per reviewer`() {
    val cycleTimePerReviewer = calculate().perReviewer

    assertThat(cycleTimePerReviewer[slowUser1]).isNull()
    assertThat(cycleTimePerReviewer[slowUser2]).isEqualTo(1.days)
    assertThat(cycleTimePerReviewer[fastUser1]).isEqualTo(3.days)
    assertThat(cycleTimePerReviewer[fastUser2]).isEqualTo(9.days + 5.hours)
  }

  @Test fun `cycle time calculation is correct per code review`() {
    val cycleTimePerCodeReview = calculate().perCodeReview

    assertThat(cycleTimePerCodeReview[slowRepo.codeReviews[0]]).isEqualTo(1.days)
    assertThat(cycleTimePerCodeReview[slowRepo.codeReviews[1]]).isEqualTo(2.days)
    assertThat(cycleTimePerCodeReview[slowRepo.codeReviews[2]]).isEqualTo(3.days)
    assertThat(cycleTimePerCodeReview[slowRepo.codeReviews[3]]).isEqualTo(4.days)
    assertThat(cycleTimePerCodeReview[fastRepo.codeReviews[0]]).isEqualTo(1.hours)
    assertThat(cycleTimePerCodeReview[fastRepo.codeReviews[1]]).isEqualTo(2.hours)
    assertThat(cycleTimePerCodeReview[fastRepo.codeReviews[2]]).isEqualTo(3.hours)
    assertThat(cycleTimePerCodeReview[fastRepo.codeReviews[3]]).isEqualTo(1.hours)
  }

  @Test fun `cycle time calculation is correct per repository`() {
    val cycleTimePerRepository = calculate().perRepository

    assertThat(cycleTimePerRepository[slowRepo]).isEqualTo(10.days)
    assertThat(cycleTimePerRepository[fastRepo]).isEqualTo(7.hours)
  }

  // region Helpers

  private val Int.days: Long
    get() = this * 24.hours

  private val Int.hours: Long
    get() = toLong() * 60 * 60 * 1000

  private val slowUser1 = User("SlowOne")
  private val slowUser2 = User("SlowTwo")
  private val fastUser2 = User("FastTwo")
  private val fastUser1 = User("FastOne")

  private val now: Instant = Instant.parse("2023-04-05T10:00:00Z")
  private fun calculate() = CycleTimeCalculator(now).calculate(listOf(slowRepo, fastRepo))

  private val slowRepo = Repository(
    "Slow owner",
    "Slow name",
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 1,
        title = "Slow one day",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 2, 10, 0, 0, 0),
        closedAt = null,
        author = slowUser1,
        requestedReviewers = listOf(slowUser2),
      ),
      Stubs.generic.codeReview.copy(
        id = 2,
        title = "Slow two days",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 3, 10, 0, 0, 0),
        closedAt = null,
        author = slowUser2,
        requestedReviewers = listOf(fastUser2),
      ),
      Stubs.generic.codeReview.copy(
        id = 3,
        title = "Slow three days",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 4, 10, 0, 0, 0),
        closedAt = null,
        author = slowUser1,
        requestedReviewers = listOf(fastUser2, fastUser1),
      ),
      Stubs.generic.codeReview.copy(
        id = 4,
        title = "Slow until now",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = null,
        closedAt = null,
        author = slowUser2,
        requestedReviewers = listOf(fastUser2),
      )
    ),
    discussions = emptyList(),
  )

  private val fastRepo = Repository(
    "Fast owner",
    "Fast name",
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 5,
        title = "Fast one hour",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 1, 11, 0, 0, 0),
        closedAt = null,
        author = fastUser1,
        requestedReviewers = listOf(fastUser2),
      ),
      Stubs.generic.codeReview.copy(
        id = 6,
        title = "Fast two hours",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = null,
        closedAt = LocalDateTime(2023, 4, 1, 12, 0, 0, 0),
        author = fastUser2,
        requestedReviewers = emptyList(),
      ),
      Stubs.generic.codeReview.copy(
        id = 7,
        title = "Fast three hours",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 1, 13, 0, 0, 0),
        closedAt = null,
        author = fastUser1,
        requestedReviewers = listOf(fastUser2),
      ),
      Stubs.generic.codeReview.copy(
        id = 8,
        title = "Surprisingly fast from slow author one",
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 1, 11, 0, 0, 0),
        closedAt = null,
        author = slowUser1,
        requestedReviewers = listOf(fastUser2),
      )
    ),
    discussions = emptyList(),
  )

  // endregion Helpers

}
