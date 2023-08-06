package components.data

import kotlinx.serialization.Serializable
import utils.HasSimpleFormat

@Serializable
data class CodeReviewChange(
  val status: Status,
  val additions: Int,
  val deletions: Int,
  val total: Int,
  val fileName: String,
) : HasSimpleFormat {

  @Serializable
  enum class Status { ADDED, REMOVED, MODIFIED, RENAMED, COPIED, CHANGED, UNCHANGED }

  override val simpleFormat = "$fileName with $total changes ${status.name.lowercase()}"

}
