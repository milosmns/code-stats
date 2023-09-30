package components.data

import kotlinx.serialization.Serializable
import utils.HasSimpleFormat

@Serializable
data class Repository(
  val owner: String,
  val name: String,
  val codeReviews: List<CodeReview>,
  val discussions: List<Discussion>,
) : HasSimpleFormat {

  @Suppress("MemberVisibilityCanBePrivate")
  val fullName: String
    get() = "$owner/$name"

  val isDead: Boolean by lazy { codeReviews.all(CodeReview::isDead) && discussions.all(Discussion::isDead) }

  override val simpleFormat = """
    |Repository $fullName
    |  · ${codeReviews.size} code reviews
    |${codeReviews.joinToString("\n") { it.simpleFormat("      ") }}
    |  · ${discussions.size} discussions
    |${discussions.joinToString("\n") { it.simpleFormat("      ") }}
  """.trimMargin()

}
