import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import models.Config
import okio.Path.Companion.toPath
import kotlin.test.Test

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
    val config = Config("owner", startDate = someWednesday)

    assertThat(config.endDate)
      .isEqualTo(LocalDate(2023, Month.JUNE, 5))
  }

  @Test fun `loading file as string works`() {
    val path = "src/commonTest/resources/test.config.yaml".toPath(normalize = true)
    val result = loadConfigFileAsString(path)

    assertThat(result.trim()).isNotEmpty()
  }

  @Test fun `loading Config from file works`() {
    val result = Config.fromFile("src/commonTest/resources/test.config.yaml")
    val expected = Config(
      owner = "milosmns",
      startDate = LocalDate(2023, Month.JANUARY, 1),
      endDate = LocalDate(2022, Month.JANUARY, 1),
      teams = listOf(
        Config.Team(
          name = "stability-chapter",
          title = "Stability Chapter",
          codeRepositories = listOf("kssm", "code-stats"),
          discussionRepositories = listOf("code-stats"),
        ),
        Config.Team(
          name = "instability-chapter",
          title = "Instability Chapter",
          codeRepositories = listOf("code-stats", "tema"),
          discussionRepositories = listOf("code-stats", "kssm"),
        ),
      ),
    )

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `parallel map works`() = runBlocking {
    val result = listOf(1, 2, 3).parallelMap { it * 2 }

    assertThat(result).isEqualTo(listOf(2, 4, 6))
  }

  @Test fun `truncate middle works for long text`() {
    assertThat("too long for display".cutMiddleTo10()).isEqualTo("too …splay")
  }

  @Test fun `truncate middle works for short text`() {
    assertThat("123456789".cutMiddleTo10()).isEqualTo("123456789")
  }

}
