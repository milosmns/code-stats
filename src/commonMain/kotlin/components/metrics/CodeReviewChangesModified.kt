package components.metrics

import components.data.CodeReview
import components.data.Discussion
import components.data.Repository
import components.data.User

data class CodeReviewChangesModified(
  override val perAuthor: Map<User, Long>,
  override val perReviewer: Map<User, Long>,
  override val perCodeReview: Map<CodeReview, Long>,
  override val perRepository: Map<Repository, Long>,
  override val perDiscussion: Map<Discussion, Long> = emptyMap(),
) : GenericLongMetric {

  override val metricName = CodeReviewChangesModified::class.simpleName!!

}
