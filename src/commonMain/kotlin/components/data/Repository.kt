package components.data

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
  val owner: String,
  val name: String,
  val codeReviews: List<CodeReview>,
  val discussions: List<Discussion>,
) {
  val fullName: String
    get() = "$owner/$name"
}
