package github.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubPullRequest(
  val id: Long,
  val number: Int,
  val state: State,
  val title: String,
  val body: String = "",
  val length: Int = body.length,
  @SerialName("user") val author: GitHubUser,
  @SerialName("requested_reviewers") val requestedReviewers: List<GitHubUser> = emptyList(),
  @SerialName("draft") val isDraft: Boolean,
  @SerialName("changed_files") val changedFiles: Int = 0,
  @SerialName("all_comments") val comments: List<Comment> = emptyList(),
  @SerialName("all_changes") val files: List<File> = emptyList(),
  @SerialName("all_reviews") val reviews: List<Review> = emptyList(),
  @SerialName("created_at") private val createdAtInstant: Instant,
  val createdAt: LocalDateTime = createdAtInstant.toLocalDateTime(TimeZone.UTC),
  @SerialName("closed_at") private val closedAtInstant: Instant? = null,
  val closedAt: LocalDateTime? = closedAtInstant?.toLocalDateTime(TimeZone.UTC),
  @SerialName("merged_at") private val mergedAtInstant: Instant? = null,
  val mergedAt: LocalDateTime? = mergedAtInstant?.toLocalDateTime(TimeZone.UTC),
) {

  @Serializable
  @Suppress("unused") // used for serialization
  enum class State {
    @SerialName("open")
    OPEN,

    @SerialName("closed")
    CLOSED,
  }

  @Serializable
  data class Comment(
    val id: Int,
    val body: String,
    val length: Int = body.length,
    @SerialName("user") val author: GitHubUser,
    @SerialName("created_at") val createdAtInstant: Instant,
    val createdAt: LocalDateTime = createdAtInstant.toLocalDateTime(TimeZone.UTC),
  )

  @Serializable
  data class File(
    val status: Status,
    val additions: Int,
    val deletions: Int,
    @SerialName("changes") val total: Int,
    @SerialName("filename") val fileName: String,
  ) {

    @Serializable
    @Suppress("unused") // used for serialization
    enum class Status {
      @SerialName("added")
      ADDED,

      @SerialName("removed")
      REMOVED,

      @SerialName("modified")
      MODIFIED,

      @SerialName("renamed")
      RENAMED,

      @SerialName("copied")
      COPIED,

      @SerialName("changed")
      CHANGED,

      @SerialName("unchanged")
      UNCHANGED,
    }

  }

  @Serializable
  data class Review(
    val id: Int,
    val body: String = "",
    val length: Int = body.length,
    val state: State,
    @SerialName("user") val author: GitHubUser,
    @SerialName("submitted_at") val submittedAtInstant: Instant? = null,
    val submittedAt: LocalDateTime? = submittedAtInstant?.toLocalDateTime(TimeZone.UTC),
  ) {

    @Serializable
    @Suppress("unused") // used for serialization
    enum class State { PENDING, APPROVED, COMMENTED, CHANGES_REQUESTED, DISMISSED }

  }

}
