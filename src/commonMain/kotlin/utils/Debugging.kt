package utils

import components.data.CodeReview
import components.data.CodeReviewComment
import components.data.CodeReviewFeedback
import components.data.Discussion
import components.data.DiscussionComment
import components.data.Repository
import kotlin.math.roundToInt

val Long.durationString: String
  get() = buildString {
    val millis = this@durationString
    if (millis == 0L) {
      append("0")
      return@buildString
    }
    val secs = millis / 1000
    val mins = secs / 60
    val hours = mins / 60
    val days = hours / 24
    val months = days / 30
    val years = months / 12
    if (years > 0) append("${years}Y ")
    if (months > 0) append("${months % 12}M ")
    if (days > 0) append("${days % 30}d ")
    if (hours > 0) append("${hours % 24}h ")
    if (mins > 0) append("${mins % 60}m ")
  }.trim()

val Float.twoDecimals: String
  get() = ((this * 100.0).roundToInt() / 100.0).toString()

class Printable(private val content: String) {
  fun onlyIf(condition: Boolean) = if (condition) print(content) else Unit
}

class PrintableLn(private val content: String) {
  fun onlyIf(condition: Boolean) = if (condition) println(content) else Unit
}

fun printable(text: String = "", indent: Int = 3) =
  Printable("${" ".repeat(indent)}$text")

fun printableLn(text: String = "", indent: Int = 3) =
  PrintableLn("${" ".repeat(indent)}$text")

fun String.ellipsis(at: Int = 20): String {
  val cleaned = this.replace("\\s+".toRegex(), " ").trim()
  if (cleaned.length <= at) return cleaned
  return cleaned.substring(0, at - 3).trim() + "..."
}

fun String.truncateMiddle(): String {
  val cleaned = this.replace("\\s+".toRegex(), " ").trim()
  if (cleaned.length <= 10) return cleaned
  return cleaned.substring(0, 4).trim() + "..." + cleaned.substring(cleaned.length - 5).trim()
}

fun CodeReviewComment.truncate() = copy(
  body = body.truncateMiddle(),
)

fun CodeReviewFeedback.truncate() = copy(
  body = body.truncateMiddle(),
)

fun CodeReview.truncate() = copy(
  body = body.truncateMiddle(),
  comments = comments.map(CodeReviewComment::truncate),
  feedbacks = feedbacks.map(CodeReviewFeedback::truncate),
)

fun DiscussionComment.truncate() = copy(
  body = body.truncateMiddle(),
)

fun Discussion.truncate() = copy(
  body = body.truncateMiddle(),
  comments = comments.map(DiscussionComment::truncate),
)

fun Repository.truncate() = copy(
  codeReviews = codeReviews.map(components.data.CodeReview::truncate),
  discussions = discussions.map(Discussion::truncate),
)
