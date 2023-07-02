package github.models

import kotlinx.datetime.LocalDateTime

data class GitHubDiscussion(
  val id: String,
  val number: Int,
  val title: String,
  val body: String,
  val createdAt: LocalDateTime,
  val closedAt: LocalDateTime?,
  val comments: List<Comment> = emptyList(),
  val author: GitHubUser,
) {

  data class Comment(
    val id: String,
    val body: String,
    val createdAt: LocalDateTime,
    val author: GitHubUser,
  )

}
