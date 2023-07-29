package components.metrics

import components.data.CodeReview
import components.data.Repository
import components.data.User

data class CycleTime(
  val perAuthor: Map<User, Long>,
  val perReviewer: Map<User, Long>,
  val perCodeReview: Map<CodeReview, Long>,
  val perRepository: Map<Repository, Long>,
) {
  val totalForAllAuthors: Long = perAuthor.values.sum()
  val averagePerAuthor: Float = totalForAllAuthors.toFloat() / perAuthor.size

  val totalForAllReviewers: Long = perReviewer.values.sum()
  val averagePerReviewer: Float = totalForAllReviewers.toFloat() / perReviewer.size

  val totalForAllCodeReviews: Long = perCodeReview.values.sum()
  val averagePerCodeReview: Float = totalForAllCodeReviews.toFloat() / perCodeReview.size

  val totalForAllRepositories: Long = perRepository.values.sum()
  val averagePerRepository: Float = totalForAllRepositories.toFloat() / perRepository.size
}
