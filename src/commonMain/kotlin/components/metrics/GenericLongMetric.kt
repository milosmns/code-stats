package components.metrics

import components.data.CodeReview
import components.data.Discussion
import components.data.Repository
import components.data.User
import utils.HasSimpleFormat
import utils.durationString

interface GenericLongMetric : HasSimpleFormat {

  val metricName: String

  val perAuthor: Map<User, Long>
  val perReviewer: Map<User, Long>
  val perCodeReview: Map<CodeReview, Long>
  val perDiscussion: Map<Discussion, Long>
  val perRepository: Map<Repository, Long>

  val totalForAllAuthors: Long
    get() = perAuthor.values.sum()

  val averagePerAuthor: Float
    get() = totalForAllAuthors.toFloat() / perAuthor.size

  val totalForAllReviewers: Long
    get() = perReviewer.values.sum()
  val averagePerReviewer: Float
    get() = totalForAllReviewers.toFloat() / perReviewer.size

  val totalForAllCodeReviews: Long
    get() = perCodeReview.values.sum()
  val averagePerCodeReview: Float
    get() = totalForAllCodeReviews.toFloat() / perCodeReview.size

  val totalForAllDiscussions: Long
    get() = perDiscussion.values.sum()
  val averagePerDiscussion: Float
    get() = totalForAllDiscussions.toFloat() / perDiscussion.size

  val totalForAllRepositories: Long
    get() = perRepository.values.sum()
  val averagePerRepository: Float
    get() = totalForAllRepositories.toFloat() / perRepository.size

  override val simpleFormat
    get() = buildString {
      appendLine(metricName)
      if (perAuthor.isNotEmpty()) {
        appendLine("  · ${perAuthor.size} authors")
        appendLine("    · Total: ${totalForAllAuthors.durationString}")
        appendLine("    · Average: ${averagePerAuthor.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perAuthor.formatOutliers())
      }
      if (perReviewer.isNotEmpty()) {
        appendLine("  · ${perReviewer.size} reviewers")
        appendLine("    · Total: ${totalForAllReviewers.durationString}")
        appendLine("    · Average: ${averagePerReviewer.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perReviewer.formatOutliers())
      }
      if (perCodeReview.isNotEmpty()) {
        appendLine("  · ${perCodeReview.size} code reviews")
        appendLine("    · Total: ${totalForAllCodeReviews.durationString}")
        appendLine("    · Average: ${averagePerCodeReview.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perCodeReview.formatOutliers())
      }
      if (perDiscussion.isNotEmpty()) {
        appendLine("  · ${perDiscussion.size} discussions")
        appendLine("    · Total: ${totalForAllDiscussions.durationString}")
        appendLine("    · Average: ${averagePerDiscussion.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perDiscussion.formatOutliers())
      }
      if (perRepository.isNotEmpty()) {
        appendLine("  · ${perRepository.size} repositories")
        appendLine("    · Total: ${totalForAllRepositories.durationString}")
        appendLine("    · Average: ${averagePerRepository.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perRepository.formatOutliers())
      }
    }

}
