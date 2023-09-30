package components.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import utils.HasSimpleFormat
import utils.durationString
import utils.ellipsis
import utils.epochMillisecondsUtc

@Serializable
data class Discussion(
  val id: String,
  val number: Int,
  val title: String,
  val body: String,
  val createdAt: LocalDateTime,
  val closedAt: LocalDateTime?,
  val comments: List<DiscussionComment>,
  val author: User,
) : HasSimpleFormat {

  val isDead: Boolean by lazy { comments.isEmpty() }

  private val lifetime = when {
    closedAt != null -> closedAt.epochMillisecondsUtc - createdAt.epochMillisecondsUtc
    else -> Clock.System.now().toEpochMilliseconds() - createdAt.epochMillisecondsUtc
  }

  private val lifetimeFormat = when {
    closedAt != null -> "Closed after ${lifetime.durationString}"
    else -> "Still open (${lifetime.durationString})"
  }

  override val simpleFormat: String
    get() = """
      |Discussion #$number · ${title.ellipsis()}
      |  "${body.ellipsis()}" at $createdAt
      |  · Total: ${comments.size} comments received
      |${comments.joinToString("\n") { it.simpleFormat("    · ") }}
      |  · $lifetimeFormat
    """.trimMargin()

}
