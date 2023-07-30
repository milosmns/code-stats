package components.metrics

import components.data.CodeReview
import components.data.Discussion
import components.data.Repository
import components.data.User

data class CycleTime(
  override val perAuthor: Map<User, Long>,
  override val perReviewer: Map<User, Long>,
  override val perCodeReview: Map<CodeReview, Long>,
  override val perDiscussion: Map<Discussion, Long>,
  override val perRepository: Map<Repository, Long>,
) : GenericLongMetric {

  override val metricName = "Cycle Time"

}
