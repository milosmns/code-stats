package components.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
  val login: String,
) {
  override fun toString() = "@$login"
}
