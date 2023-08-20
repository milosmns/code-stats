package components.metrics

import components.data.CodeReview
import components.data.Discussion
import components.data.Repository
import components.data.User

data class CodeReviewChangesAdded(
  override val perAuthor: Map<User, Long>,
  override val perReviewer: Map<User, Long>,
  override val perCodeReview: Map<CodeReview, Long>,
  override val perRepository: Map<Repository, Long>,
  override val perDiscussion: Map<Discussion, Long> = emptyMap(),
) : GenericCountMetric {

  override val name = CodeReviewChangesAdded::class.simpleName!!

}
