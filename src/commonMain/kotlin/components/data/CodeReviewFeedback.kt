package components.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import utils.HasSimpleFormat
import utils.ellipsis

@Serializable
data class CodeReviewFeedback(
  val id: Long,
  val body: String,
  val state: State,
  val author: User,
  val submittedAt: LocalDateTime?,
) : HasSimpleFormat {

  @Serializable
  enum class State { PENDING, APPROVED, COMMENTED, CHANGES_REQUESTED, DISMISSED }

  override val simpleFormat = "$author '$state' at ${submittedAt ?: "unknown time"}: \"${body.ellipsis()}\""

}
