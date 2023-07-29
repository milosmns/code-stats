package components.metrics

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import stubs.Stubs

class CycleTimeTest {

  @Test fun `long metric total for all users is correct`() {
    val result = Stubs.metrics.cycleTime.totalForAllAuthors
    val expected = 6L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per user is correct`() {
    val result = Stubs.metrics.cycleTime.averagePerAuthor
    val expected = 2.0F

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric total for all reviewers is correct`() {
    val result = Stubs.metrics.cycleTime.totalForAllReviewers
    val expected = 60L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per reviewer is correct`() {
    val result = Stubs.metrics.cycleTime.averagePerReviewer
    val expected = 20f

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric total for all code reviews is correct`() {
    val result = Stubs.metrics.cycleTime.totalForAllCodeReviews
    val expected = 600L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per code review is correct`() {
    val result = Stubs.metrics.cycleTime.averagePerCodeReview
    val expected = 200f

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric total for all repositories is correct`() {
    val result = Stubs.metrics.cycleTime.totalForAllRepositories
    val expected = 6000L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per repository is correct`() {
    val result = Stubs.metrics.cycleTime.averagePerRepository
    val expected = 2000f

    assertThat(result).isEqualTo(expected)
  }

}
