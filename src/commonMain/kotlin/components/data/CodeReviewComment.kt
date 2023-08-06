package components.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import utils.HasSimpleFormat
import utils.ellipsis

@Serializable
data class CodeReviewComment(
  val id: Long,
  val body: String,
  val author: User,
  val createdAt: LocalDateTime,
) : HasSimpleFormat {
  override val simpleFormat = "$author at $createdAt: \"${body.ellipsis()}\""
}
