package models

import getLastMondayAsLocal
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable

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

  companion object // explicit for extensions

}
