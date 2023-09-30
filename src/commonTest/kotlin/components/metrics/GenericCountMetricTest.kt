package components.metrics

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import stubs.Stubs

class GenericCountMetricTest {

  @Test fun `long metric total for all users is correct`() {
    val result = Stubs.metrics.genericCount.totalForAllAuthors
    val expected = 6L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per user is correct`() {
    val result = Stubs.metrics.genericCount.averagePerAuthor
    val expected = 2.0F

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric total for all reviewers is correct`() {
    val result = Stubs.metrics.genericCount.totalForAllReviewers
    val expected = 60L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per reviewer is correct`() {
    val result = Stubs.metrics.genericCount.averagePerReviewer
    val expected = 20f

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric total for all code reviews is correct`() {
    val result = Stubs.metrics.genericCount.totalForAllCodeReviews
    val expected = 600L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per code review is correct`() {
    val result = Stubs.metrics.genericCount.averagePerCodeReview
    val expected = 200f

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric total for all discussions is correct`() {
    val result = Stubs.metrics.genericCount.totalForAllDiscussions
    val expected = 6000L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per discussion is correct`() {
    val result = Stubs.metrics.genericCount.averagePerDiscussion
    val expected = 2000f

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric total for all repositories is correct`() {
    val result = Stubs.metrics.genericCount.totalForAllRepositories
    val expected = 60000L

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `long metric average per repository is correct`() {
    val result = Stubs.metrics.genericCount.averagePerRepository
    val expected = 20000f

    assertThat(result).isEqualTo(expected)
  }

}
