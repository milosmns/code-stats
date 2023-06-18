package models

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
  val owner: String,
  val name: String,
  val mergeRequests: List<MergeRequest> = emptyList(),
  val discussions: List<Discussion> = emptyList(),
)
