package history.storage

import codestats.CodeReviewComment as DatabaseCodeReviewComment
import codestats.codestats
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage
import models.CodeReview
import models.TeamHistoryConfig

interface CodeReviewCommentStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllCodeReviewComments(): List<CodeReview.Comment> =
    database.codeReviewCommentQueries.fetchAll().executeAsList().map(DatabaseCodeReviewComment::toGeneric)

  fun fetchOneCodeReviewComment(repoName: String, parentId: Long, id: Long): CodeReview.Comment? =
    database.codeReviewCommentQueries
      .fetchOne(teamHistoryConfig.owner, repoName, parentId, id)
      .executeAsOneOrNull()
      ?.toGeneric()

  fun fetchAllCodeReviewCommentsByParent(repoName: String, parentId: Long): List<CodeReview.Comment> =
    database.codeReviewCommentQueries
      .fetchAllByParent(teamHistoryConfig.owner, repoName, parentId)
      .executeAsList()
      .map(DatabaseCodeReviewComment::toGeneric)

  fun storeCodeReviewComment(repoName: String, parentId: Long, codeReviewComment: CodeReview.Comment) =
    database.codeReviewCommentQueries.save(codeReviewComment.toStorage(teamHistoryConfig.owner, repoName, parentId))

  fun purgeCodeReviewComments() = database.codeReviewCommentQueries.purge()

  fun deleteOneCodeReviewComment(repoName: String, parentId: Long, id: Long) =
    database.codeReviewCommentQueries.deleteOne(teamHistoryConfig.owner, repoName, parentId, id)

  fun deleteAllCodeReviewCommentsByParent(repoName: String, parentId: Long) =
    database.codeReviewCommentQueries.deleteAllByParent(teamHistoryConfig.owner, repoName, parentId)

}
