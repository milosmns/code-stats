package components.metrics

import utils.durationString

interface GenericDurationMetric : GenericCountMetric {

  override val simpleFormat
    get() = buildString {
      appendLine(name)
      if (perAuthor.isNotEmpty()) {
        appendLine("  · ${perAuthor.size} authors")
        appendLine("    · Total: ${totalForAllAuthors.durationString}")
        appendLine("    · Average: ${averagePerAuthor.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perAuthor.formatOutliersAsDuration())
      }
      if (perReviewer.isNotEmpty()) {
        appendLine("  · ${perReviewer.size} reviewers")
        appendLine("    · Total: ${totalForAllReviewers.durationString}")
        appendLine("    · Average: ${averagePerReviewer.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perReviewer.formatOutliersAsDuration())
      }
      if (perCodeReview.isNotEmpty()) {
        appendLine("  · ${perCodeReview.size} code reviews")
        appendLine("    · Total: ${totalForAllCodeReviews.durationString}")
        appendLine("    · Average: ${averagePerCodeReview.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perCodeReview.formatOutliersAsDuration())
      }
      if (perDiscussion.isNotEmpty()) {
        appendLine("  · ${perDiscussion.size} discussions")
        appendLine("    · Total: ${totalForAllDiscussions.durationString}")
        appendLine("    · Average: ${averagePerDiscussion.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perDiscussion.formatOutliersAsDuration())
      }
      if (perRepository.isNotEmpty()) {
        appendLine("  · ${perRepository.size} repositories")
        appendLine("    · Total: ${totalForAllRepositories.durationString}")
        appendLine("    · Average: ${averagePerRepository.toLong().durationString}")
        appendLine("    · Outliers:")
        appendLine(perRepository.formatOutliersAsDuration())
      }
    }

}
