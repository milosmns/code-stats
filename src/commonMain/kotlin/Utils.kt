import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import models.CodeReview
import models.Config
import models.Discussion
import models.Repository
import net.mamoe.yamlkt.Yaml
import okio.Path
import okio.Path.Companion.toPath

expect fun loadFileAsString(configPath: Path): String

expect fun readEnvVar(name: String): String?

suspend inline fun <Input, Output> Iterable<Input>.parallelMap(
  crossinline mapper: suspend (Input) -> Output,
): List<Output> = coroutineScope {
  map {
    async { mapper(it) }
  }.awaitAll()
}

fun getLastMondayAsLocal(now: Instant = Clock.System.now()): LocalDate {
  val timeZone = TimeZone.currentSystemDefault()
  val today = now.toLocalDateTime(timeZone).date
  val daysSinceMonday = today.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal
  return today.minus(daysSinceMonday, DateTimeUnit.DAY)
}

fun Config.Companion.fromFile(path: String): Config {
  if (!path.endsWith(".yaml") && !path.endsWith(".yml"))
    throw IllegalArgumentException("Must be a YAML file ($path).")

  try {
    val ioPath = path.toPath(normalize = true)
    val fileContent = loadFileAsString(ioPath)
    return Yaml.Default.decodeFromString(fileContent)
  } catch (e: Exception) {
    throw IllegalArgumentException("Could not load config file ($path)", e)
  }
}

// region Debugging tools

fun String.truncateMiddle(): String {
  val cleaned = this.replace("\\s+".toRegex(), " ").trim()
  if (cleaned.length <= 10) return cleaned
  return cleaned.substring(0, 4) + "..." + cleaned.substring(cleaned.length - 5)
}

// no need to test these, it's for debugging only
fun CodeReview.Comment.truncate() = copy(
  body = body.truncateMiddle(),
)

fun CodeReview.Feedback.truncate() = copy(
  body = body.truncateMiddle(),
)

fun CodeReview.truncate() = copy(
  body = body.truncateMiddle(),
  comments = comments.map(CodeReview.Comment::truncate),
  feedbacks = feedbacks.map(CodeReview.Feedback::truncate),
)

fun Discussion.Comment.truncate() = copy(
  body = body.truncateMiddle(),
)

fun Discussion.truncate() = copy(
  body = body.truncateMiddle(),
  comments = comments.map(Discussion.Comment::truncate),
)

fun Repository.truncate() = copy(
  codeReviews = codeReviews.map(CodeReview::truncate),
  discussions = discussions.map(Discussion::truncate),
)

// endregion
