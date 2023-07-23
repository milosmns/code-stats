package history.storage

import codestats.Discussion as DatabaseDiscussion
import codestats.codestats
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage
import models.Discussion
import models.TeamHistoryConfig

interface DiscussionStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllDiscussions(): List<Discussion> =
    database.discussionQueries.fetchAll().executeAsList().map(DatabaseDiscussion::toGeneric)

  fun fetchOneDiscussion(repoName: String, id: String): Discussion? =
    database.discussionQueries.fetchOne(teamHistoryConfig.owner, repoName, id).executeAsOneOrNull()?.toGeneric()

  fun fetchOneDiscussionByNumber(repoName: String, number: Int): Discussion? =
    database.discussionQueries
      .fetchOneByNumber(teamHistoryConfig.owner, repoName, number.toLong())
      .executeAsOneOrNull()
      ?.toGeneric()

  fun fetchAllDiscussionsByParent(repoName: String): List<Discussion> =
    database.discussionQueries.fetchAllByParent(teamHistoryConfig.owner, repoName).executeAsList()
      .map(DatabaseDiscussion::toGeneric)

  fun storeDiscussion(repoName: String, discussion: Discussion) =
    database.discussionQueries.save(discussion.toStorage(teamHistoryConfig.owner, repoName))

  fun purgeDiscussions() = database.discussionQueries.purge()

  fun deleteOneDiscussion(repoName: String, id: String) =
    database.discussionQueries.deleteOne(teamHistoryConfig.owner, repoName, id)

  fun deleteOneDiscussionByNumber(repoName: String, number: Int) =
    database.discussionQueries.deleteOneByNumber(teamHistoryConfig.owner, repoName, number.toLong())

  fun deleteAllDiscussionsByParent(repoName: String) =
    database.discussionQueries.deleteAllByParent(teamHistoryConfig.owner, repoName)

}
