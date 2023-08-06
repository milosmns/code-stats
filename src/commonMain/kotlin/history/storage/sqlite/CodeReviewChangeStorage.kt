package history.storage.sqlite

import codestats.CodeReviewChange as DatabaseCodeReviewChange
import codestats.codestats
import components.data.CodeReviewChange
import components.data.TeamHistoryConfig
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage

interface CodeReviewChangeStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllCodeReviewChanges(): List<CodeReviewChange> =
    database.codeReviewChangeQueries.fetchAll().executeAsList().map(DatabaseCodeReviewChange::toGeneric)

  fun fetchOneCodeReviewChange(repoName: String, parentId: Long, fileName: String): CodeReviewChange? =
    database.codeReviewChangeQueries
      .fetchOne(teamHistoryConfig.owner, repoName, parentId, fileName)
      .executeAsOneOrNull()
      ?.toGeneric()

  fun fetchAllCodeReviewChangesByParent(repoName: String, parentId: Long): List<CodeReviewChange> =
    database.codeReviewChangeQueries
      .fetchAllByParent(teamHistoryConfig.owner, repoName, parentId)
      .executeAsList()
      .map(DatabaseCodeReviewChange::toGeneric)

  fun storeCodeReviewChange(repoName: String, parentId: Long, codeReviewChange: CodeReviewChange) =
    database.codeReviewChangeQueries.save(codeReviewChange.toStorage(teamHistoryConfig.owner, repoName, parentId))

  fun purgeCodeReviewChanges() = database.codeReviewChangeQueries.purge()

  fun deleteOneCodeReviewChange(repoName: String, parentId: Long, fileName: String) =
    database.codeReviewChangeQueries.deleteOne(teamHistoryConfig.owner, repoName, parentId, fileName)

  fun deleteAllCodeReviewChangesByParent(repoName: String, parentId: Long) =
    database.codeReviewChangeQueries.deleteAllByParent(teamHistoryConfig.owner, repoName, parentId)

}
