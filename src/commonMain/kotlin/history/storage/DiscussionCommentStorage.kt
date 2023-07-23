package history.storage

import codestats.DiscussionComment as DatabaseDiscussionComment
import codestats.codestats
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage
import models.Discussion
import models.TeamHistoryConfig

interface DiscussionCommentStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllDiscussionComments(): List<Discussion.Comment> =
    database.discussionCommentQueries.fetchAll().executeAsList().map(DatabaseDiscussionComment::toGeneric)

  fun fetchOneDiscussionComment(repoName: String, parentId: String, id: String): Discussion.Comment? =
    database.discussionCommentQueries
      .fetchOne(teamHistoryConfig.owner, repoName, parentId, id)
      .executeAsOneOrNull()
      ?.toGeneric()

  fun fetchAllDiscussionCommentsByParent(repoName: String, parentId: String): List<Discussion.Comment> =
    database.discussionCommentQueries
      .fetchAllByParent(teamHistoryConfig.owner, repoName, parentId)
      .executeAsList()
      .map(DatabaseDiscussionComment::toGeneric)

  fun storeDiscussionComment(repoName: String, parentId: String, discussionComment: Discussion.Comment) =
    database.discussionCommentQueries.save(discussionComment.toStorage(teamHistoryConfig.owner, repoName, parentId))

  fun purgeDiscussionComments() = database.discussionCommentQueries.purge()

  fun deleteOneDiscussionComment(repoName: String, parentId: String, id: String) =
    database.discussionCommentQueries.deleteOne(teamHistoryConfig.owner, repoName, parentId, id)

  fun deleteAllDiscussionCommentsByParent(repoName: String, parentId: String) =
    database.discussionCommentQueries.deleteAllByParent(teamHistoryConfig.owner, repoName, parentId)

}
