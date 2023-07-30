package stubs

import components.metrics.GenericLongMetric

object MetricsStubs {

  val genericLong = object : GenericLongMetric {
    override val metricName = "Generic Long"
    override val perAuthor = mapOf(
      Stubs.generic.user.copy(login = "uno") to 1L,
      Stubs.generic.user.copy(login = "due") to 2L,
      Stubs.generic.user.copy(login = "tre") to 3L,
    )
    override val perReviewer = mapOf(
      Stubs.generic.user.copy(login = "uno") to 10L,
      Stubs.generic.user.copy(login = "due") to 20L,
      Stubs.generic.user.copy(login = "tre") to 30L,
    )
    override val perCodeReview = mapOf(
      Stubs.generic.codeReview.copy(number = 1) to 100L,
      Stubs.generic.codeReview.copy(number = 2) to 200L,
      Stubs.generic.codeReview.copy(number = 3) to 300L,
    )
    override val perDiscussion = mapOf(
      Stubs.generic.discussion.copy(number = 1) to 1000L,
      Stubs.generic.discussion.copy(number = 2) to 2000L,
      Stubs.generic.discussion.copy(number = 3) to 3000L,
    )
    override val perRepository = mapOf(
      Stubs.generic.repository.copy(name = "uno") to 10000L,
      Stubs.generic.repository.copy(name = "due") to 20000L,
      Stubs.generic.repository.copy(name = "tre") to 30000L,
    )
  }

}
