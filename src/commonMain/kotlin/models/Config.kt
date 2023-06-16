package models

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import okio.Buffer
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

@Serializable
data class Config(
  val owner: String,
  val startDate: LocalDate = getLastMondayAsLocal(),
  val endDate: LocalDate = startDate.minus(7, DateTimeUnit.DAY),
  val teams: List<Team> = emptyList(),
) {

  @Serializable
  data class Team(
    val name: String,
    val title: String = name,
    val codeRepositories: List<String> = emptyList(),
    val discussionRepositories: List<String> = emptyList(),
  )

  companion object {
    fun fromFile(path: String): Config {
      if (!path.endsWith(".yaml") && !path.endsWith(".yml"))
        throw IllegalArgumentException("Must be a YAML file ($path).")

      val ioPath = path.toPath(normalize = true)
      val fileContent = loadConfigFileAsString(ioPath)
      return Yaml.Default.decodeFromString(fileContent)
    }
  }

}

fun loadConfigFileAsString(configPath: Path): String {
  return Buffer().apply {
    FileSystem.SYSTEM.source(configPath).use { source ->
      source.buffer().readAll(this)
    }
  }.readUtf8()
}

fun getLastMondayAsLocal(now: Instant = Clock.System.now()): LocalDate {
  val timeZone = TimeZone.currentSystemDefault()
  val today = now.toLocalDateTime(timeZone).date
  val daysSinceMonday = today.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal
  return today.minus(daysSinceMonday, DateTimeUnit.DAY)
}
