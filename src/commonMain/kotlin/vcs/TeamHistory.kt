package vcs

import models.CodeReview
import models.Config
import models.Discussion
import models.Repository

interface TeamHistory {

  val config: Config

  suspend fun fetchCodeReview(repository: String, number: Int): CodeReview

  suspend fun fetchCodeReviews(repository: String): List<CodeReview>

  suspend fun fetchDiscussion(repository: String, number: Int): Discussion

  suspend fun fetchDiscussions(repository: String): List<Discussion>

  suspend fun fetchRepository(
    repository: String,
    excludeCodeReviews: Boolean = false,
    excludeDiscussions: Boolean = false,
  ): Repository

  fun clean()

}
