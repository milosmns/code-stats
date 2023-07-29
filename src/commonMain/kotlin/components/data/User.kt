package components.data

import kotlinx.serialization.Serializable
import utils.HasSimpleFormat

@Serializable
data class User(
  val login: String,
) : HasSimpleFormat {
  override fun toString() = "@$login"
  override val simpleFormat = toString()
}
