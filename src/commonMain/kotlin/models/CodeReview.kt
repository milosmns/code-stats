package models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

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
  val changedFiles: Int,
  val comments: List<Comment>,
  val changes: List<Changes>,
  val feedbacks: List<Feedback>,
  val createdAt: LocalDateTime,
  val closedAt: LocalDateTime?,
  val mergedAt: LocalDateTime?,
) {

  @Serializable
  enum class State { OPEN, CLOSED }

  @Serializable
  data class Comment(
    val id: Long,
    val body: String,
    val author: User,
    val createdAt: LocalDateTime,
  )

  @Serializable
  data class Changes(
    val status: Status,
    val additions: Int,
    val deletions: Int,
    val total: Int,
    val fileName: String,
  ) {

    @Serializable
    enum class Status { ADDED, REMOVED, MODIFIED, RENAMED, COPIED, CHANGED, UNCHANGED }

  }

  @Serializable
  data class Feedback(
    val id: Long,
    val body: String,
    val state: State,
    val author: User,
    val submittedAt: LocalDateTime?,
  ) {

    @Serializable
    enum class State { PENDING, APPROVED, COMMENTED, CHANGES_REQUESTED, DISMISSED }

  }

}
