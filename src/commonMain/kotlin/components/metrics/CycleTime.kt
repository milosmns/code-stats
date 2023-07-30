package components.metrics

import components.data.CodeReview
import components.data.Repository
import components.data.User
import utils.HasSimpleFormat
import utils.durationString
import utils.ellipsis

data class CycleTime(
  val perAuthor: Map<User, Long>,
  val perReviewer: Map<User, Long>,
  val perCodeReview: Map<CodeReview, Long>,
  val perRepository: Map<Repository, Long>,
) : HasSimpleFormat {

  val totalForAllAuthors: Long = perAuthor.values.sum()
  val averagePerAuthor: Float = totalForAllAuthors.toFloat() / perAuthor.size

  val totalForAllReviewers: Long = perReviewer.values.sum()
  val averagePerReviewer: Float = totalForAllReviewers.toFloat() / perReviewer.size

  val totalForAllCodeReviews: Long = perCodeReview.values.sum()
  val averagePerCodeReview: Float = totalForAllCodeReviews.toFloat() / perCodeReview.size

  val totalForAllRepositories: Long = perRepository.values.sum()
  val averagePerRepository: Float = totalForAllRepositories.toFloat() / perRepository.size

  override val simpleFormat = """
    |Cycle Time
    |  · ${perAuthor.size} authors
    |    · Total: ${totalForAllAuthors.durationString}
    |    · Average: ${averagePerAuthor.toLong().durationString}
    |    · Outliers:
    |${perAuthor.outliersFormat}
    |  · ${perReviewer.size} reviewers
    |    · Total: ${totalForAllReviewers.durationString}
    |    · Average: ${averagePerReviewer.toLong().durationString}
    |    · Outliers:
    |${perReviewer.outliersFormat}
    |  · ${perCodeReview.size} code reviews
    |    · Total: ${totalForAllCodeReviews.durationString}
    |    · Average: ${averagePerCodeReview.toLong().durationString}
    |    · Outliers:
    |${perCodeReview.outliersFormat}
    |  · ${perRepository.size} repositories
    |    · Total: ${totalForAllRepositories.durationString}
    |    · Average: ${averagePerRepository.toLong().durationString}
    |    · Outliers:
    |${perRepository.outliersFormat}
  """.trimMargin()

  private val <T : HasSimpleFormat> Map<T, Long>.top3: List<Pair<T, Long>>
    get() = toList()
      .sortedByDescending { (_, value) -> value }
      .take(3)

  private val <T : HasSimpleFormat> Map<T, Long>.bottom3: List<Pair<T, Long>>
    get() = toList()
      .sortedBy { (_, value) -> value }
      .take(3)

  private val <T : HasSimpleFormat> Map<T, Long>.outliersFormat: String
    get() = buildString {
      if (top3.isEmpty() && bottom3.isEmpty()) {
        append("      · None")
        return@buildString
      }
      if (top3.isNotEmpty()) {
        appendLine("      · Top 3:")
        appendLine(
          top3.joinToString("\n") { "        · ${it.first.simpleFormat.ellipsis(50)} (${it.second.durationString})" }
        )
      }
      if (bottom3.isNotEmpty()) {
        appendLine("      · Bottom 3:")
        appendLine(
          bottom3.joinToString("\n") { "        · ${it.first.simpleFormat.ellipsis(50)} (${it.second.durationString})" }
        )
      }
    }

}
