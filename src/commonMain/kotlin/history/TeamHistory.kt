package history

import components.data.CodeReview
import components.data.Discussion
import components.data.Repository
import components.data.TeamHistoryConfig
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
