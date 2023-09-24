package utils

import components.data.TeamHistoryConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
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

fun <K, V : Comparable<V>> Map<K, V>.getTop(n: Int = 1): List<Pair<K, V>> =
  toList()
    .sortedByDescending { (_, value) -> value }
    .take(n)

fun <K, V : Comparable<V>> Map<K, V>.getBottom(n: Int = 1): List<Pair<K, V>> =
  toList()
    .sortedByDescending { (_, value) -> value }
    .takeLast(n)

fun getLastMondayAsLocal(now: Instant = Clock.System.now()): LocalDate {
  val timeZone = TimeZone.currentSystemDefault()
  val today = now.toLocalDateTime(timeZone).date
  val daysSinceMonday = today.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal
  return today.minus(daysSinceMonday, DateTimeUnit.DAY)
}

fun TeamHistoryConfig.Companion.fromFile(path: String): TeamHistoryConfig {
  if (!path.endsWith(".yaml") && !path.endsWith(".yml"))
    throw IllegalArgumentException("Must be a YAML file ($path).\n  Found: $path")

  try {
    val ioPath = path.toPath(normalize = true)
    val fileContent = loadFileAsString(ioPath)
    val config = Yaml.Default.decodeFromString<TeamHistoryConfig>(fileContent)
    return config.sorted()
  } catch (e: Exception) {
    throw IllegalArgumentException("Failed to load team config from $path.\n Error: ${e.message}", e)
  }
}

val LocalDateTime.epochMillisecondsUtc: Long
  get() = toInstant(TimeZone.UTC).toEpochMilliseconds()

val Int.days: Long
  get() = this * 24.hours

val Int.hours: Long
  get() = toLong() * 60 * 60 * 1000

private fun TeamHistoryConfig.sorted() = copy(
  teams = teams.sortedBy { it.name }
    .map { team ->
      team.copy(
        codeRepositories = team.codeRepositories.sorted(),
        discussionRepositories = team.discussionRepositories.sorted(),
      )
    }
)
