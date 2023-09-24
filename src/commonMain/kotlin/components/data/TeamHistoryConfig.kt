package components.data

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable
import utils.HasSimpleFormat
import utils.getLastMondayAsLocal

@Serializable
data class TeamHistoryConfig(
  val owner: String,
  val startDate: LocalDate = getLastMondayAsLocal(),
  val endDate: LocalDate = startDate.minus(7, DateTimeUnit.DAY),
  val teams: List<Team> = emptyList(),
) : HasSimpleFormat {

  companion object // explicit for extensions

  @Serializable
  data class Team(
    val name: String,
    val title: String = name,
    val codeRepositories: List<String> = emptyList(),
    val discussionRepositories: List<String> = emptyList(),
  ) : HasSimpleFormat {
    override val simpleFormat =
      "$title (${codeRepositories.size} code repos, ${discussionRepositories.size} discussion repos)"
  }

  override val simpleFormat: String
    get() = """
      |Team History of $owner from $startDate to $endDate
      |  · ${teams.size} teams
      |${teams.joinToString("\n") { it.simpleFormat("    · ") }}
      |  · Code repositories: ${teams.sumOf { it.codeRepositories.size }}
      |  · Discussion repositories: ${teams.sumOf { it.discussionRepositories.size }}
    """.trimMargin()

}
