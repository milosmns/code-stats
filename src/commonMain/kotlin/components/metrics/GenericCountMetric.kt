package components.metrics

import components.data.CodeReview
import components.data.Discussion
import components.data.Repository
import components.data.User
import utils.HasSimpleFormat
import utils.twoDecimals

interface GenericCountMetric : HasSimpleFormat {

  val name: String
  val unit: String get() = "count"

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

  val serializable: SerializableGenericCountMetric
    get() = SerializableGenericCountMetric(
      name = name,
      unit = unit,
      perAuthor = perAuthor.mapKeys { it.key.login },
      perReviewer = perReviewer.mapKeys { it.key.login },
      perCodeReview = perCodeReview.mapKeys { "#${it.key.number}" },
      perDiscussion = perDiscussion.mapKeys { "#${it.key.number}" },
      perRepository = perRepository.mapKeys { it.key.fullName },
      totalForAllAuthors = totalForAllAuthors,
      averagePerAuthor = if (averagePerAuthor.isNaN()) null else averagePerAuthor,
      totalForAllReviewers = totalForAllReviewers,
      averagePerReviewer = if (averagePerReviewer.isNaN()) null else averagePerReviewer,
      totalForAllCodeReviews = totalForAllCodeReviews,
      averagePerCodeReview = if (averagePerCodeReview.isNaN()) null else averagePerCodeReview,
      totalForAllDiscussions = totalForAllDiscussions,
      averagePerDiscussion = if (averagePerDiscussion.isNaN()) null else averagePerDiscussion,
      totalForAllRepositories = totalForAllRepositories,
      averagePerRepository = if (averagePerRepository.isNaN()) null else averagePerRepository,
    )

  override val simpleFormat
    get() = buildString {
      appendLine(name)
      if (perAuthor.isNotEmpty()) {
        appendLine("  · ${perAuthor.size} authors")
        appendLine("    · Total: $totalForAllAuthors")
        appendLine("    · Average: ${averagePerAuthor.twoDecimals}")
        appendLine("    · Outliers:")
        appendLine(perAuthor.formatOutliersAsCount())
      }
      if (perReviewer.isNotEmpty()) {
        appendLine("  · ${perReviewer.size} reviewers")
        appendLine("    · Total: $totalForAllReviewers")
        appendLine("    · Average: ${averagePerReviewer.twoDecimals}")
        appendLine("    · Outliers:")
        appendLine(perReviewer.formatOutliersAsCount())
      }
      if (perCodeReview.isNotEmpty()) {
        appendLine("  · ${perCodeReview.size} code reviews")
        appendLine("    · Total: $totalForAllCodeReviews")
        appendLine("    · Average: ${averagePerCodeReview.twoDecimals}")
        appendLine("    · Outliers:")
        appendLine(perCodeReview.formatOutliersAsCount())
      }
      if (perDiscussion.isNotEmpty()) {
        appendLine("  · ${perDiscussion.size} discussions")
        appendLine("    · Total: $totalForAllDiscussions")
        appendLine("    · Average: ${averagePerDiscussion.twoDecimals}")
        appendLine("    · Outliers:")
        appendLine(perDiscussion.formatOutliersAsCount())
      }
      if (perRepository.isNotEmpty()) {
        appendLine("  · ${perRepository.size} repositories")
        appendLine("    · Total: $totalForAllRepositories")
        appendLine("    · Average: ${averagePerRepository.twoDecimals}")
        appendLine("    · Outliers:")
        appendLine(perRepository.formatOutliersAsCount())
      }
    }

}
