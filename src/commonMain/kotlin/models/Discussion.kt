package models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Discussion(
  val id: String,
  val number: Int,
  val title: String,
  val body: String,
  val length: Int = body.length,
  val createdAtInstant: Instant,
  val createdAt: LocalDateTime = createdAtInstant.toLocalDateTime(TimeZone.UTC),
  val comments: List<Comment> = emptyList(),
  @SerialName("user") val author: User,
) {

  @Serializable
  data class Comment(
    val length: Int,
    val createdAtInstant: Instant,
    val createdAt: LocalDateTime = createdAtInstant.toLocalDateTime(TimeZone.UTC),
    @SerialName("user") val author: User,
  )

}
