package components.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Discussion(
  val id: String,
  val number: Int,
  val title: String,
  val body: String,
  val createdAt: LocalDateTime,
  val closedAt: LocalDateTime?,
  val comments: List<Comment>,
  val author: User,
) {

  @Serializable
  data class Comment(
    val id: String,
    val body: String,
    val createdAt: LocalDateTime,
    val author: User,
  )

}
