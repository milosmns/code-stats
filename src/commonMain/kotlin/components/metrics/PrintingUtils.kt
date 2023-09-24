package components.metrics

import utils.HasSimpleFormat
import utils.durationString
import utils.ellipsis
import utils.getBottom
import utils.getTop

fun <T : HasSimpleFormat> Map<T, Long>.formatOutliersAsDuration(
  cutAt: Int = 40, // characters
  places: Int = 3, // top and bottom
  firstLinePrefix: String = "      ·", // 6 spaces + bullet
  linePrefix: String = "  $firstLinePrefix", // with 2 prefix spaces
  outlierFormatter: (Pair<T, Long>) -> String = { (owner, duration) ->
    "$linePrefix ${owner.simpleFormat.ellipsis(cutAt)} (${duration.durationString})"
  },
) = formatOutliers(
  places = places,
  firstLinePrefix = firstLinePrefix,
  outlierFormatter = outlierFormatter,
)

fun <T : HasSimpleFormat> Map<T, Long>.formatOutliersAsCount(
  cutAt: Int = 50, // characters
  places: Int = 3, // top and bottom
  firstLinePrefix: String = "      ·", // 6 spaces + bullet
  linePrefix: String = "  $firstLinePrefix", // with 2 prefix spaces
  outlierFormatter: (Pair<T, Long>) -> String = { (owner, count) ->
    "$linePrefix ${owner.simpleFormat.ellipsis(cutAt)} ($count)"
  },
) = formatOutliers(
  places = places,
  firstLinePrefix = firstLinePrefix,
  outlierFormatter = outlierFormatter,
)

fun <T : HasSimpleFormat> Map<T, Long>.formatOutliers(
  places: Int, // top and bottom
  firstLinePrefix: String,
  outlierFormatter: (Pair<T, Long>) -> String,
) = buildString {
  when {
    this@formatOutliers.isEmpty() -> append("$firstLinePrefix None")

    this@formatOutliers.size <= places -> appendLine(
      this@formatOutliers.getTop(places).joinToString("\n", transform = outlierFormatter)
    )

    else -> {
      getTop(places).let { top ->
        appendLine("$firstLinePrefix Top $places:")
        appendLine(top.joinToString("\n", transform = outlierFormatter))
      }
      getBottom(places).let { bottom ->
        appendLine("$firstLinePrefix Bottom $places:")
        appendLine(bottom.joinToString("\n", transform = outlierFormatter))
      }
    }
  }
}
