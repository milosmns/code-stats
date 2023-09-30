package components.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import utils.HasSimpleFormat
import utils.durationString
import utils.ellipsis
import utils.epochMillisecondsUtc

@Serializable
data class CodeReview(
  val id: Long,
  val number: Int,
  val state: State,
  val title: String,
  val body: String,
  val author: User,
  val requestedReviewers: List<User>,
  val isDraft: Boolean,
  val comments: List<CodeReviewComment>,
  val changes: List<CodeReviewChange>,
  val feedbacks: List<CodeReviewFeedback>,
  val createdAt: LocalDateTime,
  val closedAt: LocalDateTime?,
  val mergedAt: LocalDateTime?,
) : HasSimpleFormat {

  @Serializable
  enum class State { OPEN, CLOSED }

  val isDead: Boolean by lazy { comments.isEmpty() && feedbacks.isEmpty() }

  private val lifetime = when {
    mergedAt != null -> mergedAt.epochMillisecondsUtc - createdAt.epochMillisecondsUtc
    closedAt != null -> closedAt.epochMillisecondsUtc - createdAt.epochMillisecondsUtc
    else -> Clock.System.now().toEpochMilliseconds() - createdAt.epochMillisecondsUtc
  }

  private val lifetimeFormat = when {
    mergedAt != null -> "Merged after ${lifetime.durationString}"
    closedAt != null -> "Closed after ${lifetime.durationString}"
    else -> "Still open (${lifetime.durationString})"
  }

  override val simpleFormat = """
    |[$state] Code Review #$number · ${title.ellipsis()}
    |  "${body.ellipsis()}" at $createdAt
    |  · ${requestedReviewers.size} reviewers requested
    |  · ${changes.size} files changed (${changes.sumOf { it.total }} lines)
    |  · ${feedbacks.size} feedbacks received
    |  · ${comments.size} comments received
    |  · $lifetimeFormat
  """.trimMargin()

}
