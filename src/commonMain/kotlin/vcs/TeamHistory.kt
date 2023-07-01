package vcs

import models.CodeReview
import models.Discussion
import models.Repository
import models.TeamHistoryConfig
import okio.Closeable

interface TeamHistory : Closeable {

  val teamHistoryConfig: TeamHistoryConfig

  suspend fun fetchCodeReview(repository: String, number: Int): CodeReview

  suspend fun fetchCodeReviews(repository: String): List<CodeReview>

  suspend fun fetchDiscussion(repository: String, number: Int): Discussion

  suspend fun fetchDiscussions(repository: String): List<Discussion>

  suspend fun fetchRepository(
    repository: String,
    includeCodeReviews: Boolean,
    includeDiscussions: Boolean,
  ): Repository

}
