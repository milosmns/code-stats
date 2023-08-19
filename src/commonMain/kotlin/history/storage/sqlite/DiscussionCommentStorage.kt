package history.storage.sqlite

import codestats.DiscussionComment as DatabaseDiscussionComment
import codestats.codestats
import components.data.DiscussionComment
import components.data.TeamHistoryConfig
import history.storage.mapping.toGeneric
import history.storage.mapping.toStorage

interface DiscussionCommentStorage {

  val teamHistoryConfig: TeamHistoryConfig
  val database: codestats

  fun fetchAllDiscussionComments(): List<DiscussionComment> =
    database.discussionCommentQueries.fetchAll().executeAsList().map(DatabaseDiscussionComment::toGeneric)

  fun fetchOneDiscussionComment(repoName: String, parentId: String, id: String): DiscussionComment? =
    database.discussionCommentQueries
      .fetchOne(teamHistoryConfig.owner, repoName, parentId, id)
      .executeAsOneOrNull()
      ?.toGeneric()

  fun fetchAllDiscussionCommentsByParent(repoName: String, parentId: String): List<DiscussionComment> =
    database.discussionCommentQueries
      .fetchAllByParent(teamHistoryConfig.owner, repoName, parentId)
      .executeAsList()
      .map(DatabaseDiscussionComment::toGeneric)

  fun storeDiscussionComment(repoName: String, parentId: String, discussionComment: DiscussionComment) =
    database.discussionCommentQueries.save(discussionComment.toStorage(teamHistoryConfig.owner, repoName, parentId))

  fun purgeDiscussionComments() = database.discussionCommentQueries.purge()

  fun deleteOneDiscussionComment(repoName: String, parentId: String, id: String) =
    database.discussionCommentQueries.deleteOne(teamHistoryConfig.owner, repoName, parentId, id)

  fun deleteAllDiscussionCommentsByParent(repoName: String, parentId: String) =
    database.discussionCommentQueries.deleteAllByParent(teamHistoryConfig.owner, repoName, parentId)

}
