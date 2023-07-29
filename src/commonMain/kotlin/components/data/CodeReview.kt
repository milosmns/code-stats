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
  val comments: List<Comment>,
  val changes: List<Change>,
  val feedbacks: List<Feedback>,
  val createdAt: LocalDateTime,
  val closedAt: LocalDateTime?,
  val mergedAt: LocalDateTime?,
) : HasSimpleFormat {

  @Serializable
  enum class State { OPEN, CLOSED }

  @Serializable
  data class Comment(
    val id: Long,
    val body: String,
    val author: User,
    val createdAt: LocalDateTime,
  ) : HasSimpleFormat {
    override val simpleFormat = "$author at $createdAt: \"${body.ellipsis()}\""
  }

  @Serializable
  data class Change(
    val status: Status,
    val additions: Int,
    val deletions: Int,
    val total: Int,
    val fileName: String,
  ) : HasSimpleFormat {

    @Serializable
    enum class Status { ADDED, REMOVED, MODIFIED, RENAMED, COPIED, CHANGED, UNCHANGED }

    override val simpleFormat = "$fileName with $total changes ${status.name.lowercase()}"

  }

  @Serializable
  data class Feedback(
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
