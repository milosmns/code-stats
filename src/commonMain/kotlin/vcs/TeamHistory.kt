package vcs

import models.Config
import models.Discussion
import models.MergeRequest
import models.Repository

interface TeamHistory {

  val config: Config

  suspend fun fetchMergeRequest(repository: String, number: Int): MergeRequest

  suspend fun fetchDiscussion(repository: String, number: Int): Discussion

  suspend fun fetchMergeRequestsByDate(repository: String): List<MergeRequest>

  suspend fun fetchDiscussionsByDate(repository: String): List<Discussion>

  suspend fun fetchRepository(repository: String): Repository

  fun clean()

}
