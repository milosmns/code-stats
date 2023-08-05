package components.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import utils.HasSimpleFormat
import utils.ellipsis

@Serializable
data class DiscussionComment(
  val id: String,
  val body: String,
  val createdAt: LocalDateTime,
  val author: User,
) : HasSimpleFormat {
  override val simpleFormat = "$author at $createdAt: \"${body.ellipsis()}\""
}
