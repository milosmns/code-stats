package components.metrics

import kotlinx.serialization.Serializable

@Serializable
data class SerializableGenericCountMetric(
  val name: String,
  val perAuthor: Map<String, Long>,
  val perReviewer: Map<String, Long>,
  val perCodeReview: Map<String, Long>,
  val perDiscussion: Map<String, Long>,
  val perRepository: Map<String, Long>,
  val totalForAllAuthors: Long,
  val averagePerAuthor: Float,
  val totalForAllReviewers: Long,
  val averagePerReviewer: Float,
  val totalForAllCodeReviews: Long,
  val averagePerCodeReview: Float,
  val totalForAllDiscussions: Long,
  val averagePerDiscussion: Float,
  val totalForAllRepositories: Long,
  val averagePerRepository: Float,
)
