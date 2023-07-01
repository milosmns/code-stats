package github.models

import kotlinx.serialization.Serializable

@Serializable
data class GitHubUser(
  val login: String,
) {
  override fun toString() = "@$login"
}
