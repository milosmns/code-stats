package history.storage.sqlite

import codestats.CodeReviewFeedback as DatabaseCodeReviewFeedback
import codestats.codestats
import components.data.CodeReviewFeedback
import components.data.TeamHistoryConfig
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage

interface CodeReviewFeedbackStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllCodeReviewFeedbacks(): List<CodeReviewFeedback> =
    database.codeReviewFeedbackQueries.fetchAll().executeAsList().map(DatabaseCodeReviewFeedback::toGeneric)

  fun fetchOneCodeReviewFeedback(repoName: String, parentId: Long, id: Long): CodeReviewFeedback? =
    database.codeReviewFeedbackQueries
      .fetchOne(teamHistoryConfig.owner, repoName, parentId, id)
      .executeAsOneOrNull()
      ?.toGeneric()

  fun fetchAllCodeReviewFeedbacksByParent(repoName: String, parentId: Long): List<CodeReviewFeedback> =
    database.codeReviewFeedbackQueries
      .fetchAllByParent(teamHistoryConfig.owner, repoName, parentId)
      .executeAsList()
      .map(DatabaseCodeReviewFeedback::toGeneric)

  fun storeCodeReviewFeedback(repoName: String, parentId: Long, codeReviewFeedback: CodeReviewFeedback) =
    database.codeReviewFeedbackQueries.save(codeReviewFeedback.toStorage(teamHistoryConfig.owner, repoName, parentId))

  fun purgeCodeReviewFeedbacks() = database.codeReviewFeedbackQueries.purge()

  fun deleteOneCodeReviewFeedback(repoName: String, parentId: Long, id: Long) =
    database.codeReviewFeedbackQueries.deleteOne(teamHistoryConfig.owner, repoName, parentId, id)

  fun deleteAllCodeReviewFeedbacksByParent(repoName: String, parentId: Long) =
    database.codeReviewFeedbackQueries.deleteAllByParent(teamHistoryConfig.owner, repoName, parentId)

}
