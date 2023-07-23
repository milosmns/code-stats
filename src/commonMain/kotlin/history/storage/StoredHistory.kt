package history.storage

import codestats.codestats
import models.Repository
import models.TeamHistoryConfig
import models.User

class StoredHistory(
  override val teamHistoryConfig: TeamHistoryConfig,
  override val database: codestats,
) : RepositoryStorage,
  UserStorage,
  DiscussionStorage,
  DiscussionCommentStorage,
  CodeReviewStorage,
  CodeReviewCommentStorage,
  CodeReviewChangeStorage,
  CodeReviewFeedbackStorage {

  fun fetchRepository(name: String, includeCodeReviews: Boolean, includeDiscussions: Boolean): Repository {
    var repository = Repository(teamHistoryConfig.owner, name, emptyList(), emptyList())
    if (includeDiscussions) {
      val discussions = fetchAllDiscussionsByParent(name).map {
        it.copy(comments = fetchAllDiscussionCommentsByParent(name, it.id))
      }
      repository = repository.copy(discussions = discussions)
    }
    if (includeCodeReviews) {
      val codeReviews = fetchAllCodeReviewsByParent(name).map {
        it.copy(
          comments = fetchAllCodeReviewCommentsByParent(name, it.id),
          changes = fetchAllCodeReviewChangesByParent(name, it.id),
          feedbacks = fetchAllCodeReviewFeedbacksByParent(name, it.id),
        )
      }
      repository = repository.copy(codeReviews = codeReviews)
    }
    return repository
  }

  fun storeRepositoryDeep(repository: Repository) {
    storeRepository(repository)
    repository.participants.forEach { storeUser(it) }
    repository.discussions.forEach { discussion ->
      storeDiscussion(repository.name, discussion)
      // discussion components require the discussion to exist
      discussion.comments.forEach { comment ->
        storeDiscussionComment(repository.name, discussion.id, comment)
      }
    }
    repository.codeReviews.forEach { codeReview ->
      storeCodeReview(repository.name, codeReview)
      // code review components require the code review to exist
      codeReview.comments.forEach { comment ->
        storeCodeReviewComment(repository.name, codeReview.id, comment)
      }
      codeReview.changes.forEach { change ->
        storeCodeReviewChange(repository.name, codeReview.id, change)
      }
      codeReview.feedbacks.forEach { feedback ->
        storeCodeReviewFeedback(repository.name, codeReview.id, feedback)
      }
    }
  }

  private val Repository.participants: Set<User>
    get() {
      val users = mutableSetOf<User>()
      discussions.forEach { discussion ->
        discussion.comments.forEach { users += it.author }
        users += discussion.author
      }
      codeReviews.forEach { codeReview ->
        codeReview.comments.forEach { users += it.author }
        codeReview.feedbacks.forEach { users += it.author }
        users += codeReview.author
      }
      return users
    }

}
