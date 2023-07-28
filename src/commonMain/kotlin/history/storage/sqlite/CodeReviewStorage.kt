package history.storage.sqlite

import codestats.CodeReview as DatabaseCodeReview
import codestats.codestats
import components.data.CodeReview
import components.data.TeamHistoryConfig
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage

interface CodeReviewStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllCodeReviews(): List<CodeReview> =
    database.codeReviewQueries.fetchAll().executeAsList().map(DatabaseCodeReview::toGeneric)

  fun fetchOneCodeReview(repoName: String, id: Long): CodeReview? =
    database.codeReviewQueries.fetchOne(teamHistoryConfig.owner, repoName, id).executeAsOneOrNull()?.toGeneric()

  fun fetchOneCodeReviewByNumber(repoName: String, number: Int): CodeReview? =
    database.codeReviewQueries
      .fetchOneByNumber(teamHistoryConfig.owner, repoName, number.toLong())
      .executeAsOneOrNull()
      ?.toGeneric()

  fun fetchAllCodeReviewsByParent(repoName: String): List<CodeReview> =
    database.codeReviewQueries.fetchAllByParent(teamHistoryConfig.owner, repoName).executeAsList()
      .map(DatabaseCodeReview::toGeneric)

  fun storeCodeReview(repoName: String, codeReview: CodeReview) =
    database.codeReviewQueries.save(codeReview.toStorage(teamHistoryConfig.owner, repoName))

  fun purgeCodeReviews() = database.codeReviewQueries.purge()

  fun deleteOneCodeReview(repoName: String, id: Long) =
    database.codeReviewQueries.deleteOne(teamHistoryConfig.owner, repoName, id)

  fun deleteOneCodeReviewByNumber(repoName: String, number: Int) =
    database.codeReviewQueries.deleteOneByNumber(teamHistoryConfig.owner, repoName, number.toLong())

  fun deleteAllCodeReviewsByParent(repoName: String) =
    database.codeReviewQueries.deleteAllByParent(teamHistoryConfig.owner, repoName)

}
