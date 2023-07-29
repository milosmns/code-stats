import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import components.data.TeamHistoryConfig
import okio.Path.Companion.toPath
import kotlin.test.Test
import utils.fromFile
import utils.getLastMondayAsLocal
import utils.loadFileAsString
import utils.parallelMap

class UtilsTest {

  @Test fun `getting last Monday works`() {
    val someWednesdayMillis = LocalDate(2023, Month.JUNE, 14)
      .atStartOfDayIn(TimeZone.UTC)
      .toEpochMilliseconds()
    val someWednesdayInstant = Instant.fromEpochMilliseconds(someWednesdayMillis)

    assertThat(getLastMondayAsLocal(now = someWednesdayInstant))
      .isEqualTo(LocalDate(2023, Month.JUNE, 12))
  }

  @Test fun `starting with 7 days prior works`() {
    val someWednesday = LocalDate(2023, Month.JUNE, 12)
    val config = TeamHistoryConfig("owner", startDate = someWednesday)

    assertThat(config.endDate)
      .isEqualTo(LocalDate(2023, Month.JUNE, 5))
  }

  @Test fun `loading file as string works`() {
    val path = "src/commonTest/resources/test.config.yaml".toPath(normalize = true)
    val result = loadFileAsString(path)

    assertThat(result.trim()).isNotEmpty()
  }

  @Test fun `loading Config from file works`() {
    val result = TeamHistoryConfig.fromFile("src/commonTest/resources/test.config.yaml")
    val expected = TeamHistoryConfig(
      owner = "milosmns",
      startDate = LocalDate(2023, Month.JANUARY, 1),
      endDate = LocalDate(2022, Month.JANUARY, 1),
      teams = listOf(
        TeamHistoryConfig.Team(
          name = "instability-chapter",
          title = "Instability Chapter",
          codeRepositories = listOf("code-stats", "tema"),
          discussionRepositories = listOf("code-stats", "kssm"),
        ),
        TeamHistoryConfig.Team(
          name = "stability-chapter",
          title = "Stability Chapter",
          codeRepositories = listOf("code-stats", "kssm"),
          discussionRepositories = listOf("code-stats"),
        ),
      ),
    )

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `parallel map works`() = runBlocking {
    val result = listOf(1, 2, 3).parallelMap { it * 2 }

    assertThat(result).isEqualTo(listOf(2, 4, 6))
  }

}
