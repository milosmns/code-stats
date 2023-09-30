package calculator

import components.data.Repository
import components.metrics.DiscussionCommentsAuthored
import history.filter.transform.RepositoryCreatedAtTransform
import kotlinx.datetime.LocalDate

class DiscussionCommentsAuthoredCalculator : GenericCountMetricCalculator<DiscussionCommentsAuthored> {

  override fun calculate(repositories: List<Repository>): DiscussionCommentsAuthored {
    val perUser = repositories
      .flatMap { repository -> repository.discussions }
      .flatMap { discussion -> discussion.comments }
      .groupBy { comment -> comment.author }
      .mapValues { (author, comments) ->
        val commentsTotal = comments.count().toLong()
        val discussionsTotal = repositories
          .flatMap { repository -> repository.discussions }
          .count { discussion -> discussion.author == author }
          .toLong()
        commentsTotal + discussionsTotal
      }

    val perReviewer = repositories
      .flatMap { repository -> repository.discussions }
      .flatMap { discussion ->
        // reviewer is anyone who commented on the discussion except the author
        discussion.comments.filter { comment -> comment.author != discussion.author }
      }
      .groupBy { comment -> comment.author }
      .mapValues { (_, comments) -> comments.size.toLong() }

    val perDiscussion = repositories
      .flatMap { repository -> repository.discussions }
      .associateWith { discussion -> 1 + discussion.comments.count().toLong() }

    val perRepository = repositories
      .associateWith { repository ->
        val commentsTotal = repository.discussions.flatMap { discussion -> discussion.comments }.count().toLong()
        val discussionsTotal = repository.discussions.count().toLong()
        commentsTotal + discussionsTotal
      }

    return DiscussionCommentsAuthored(
      perAuthor = perUser,
      perReviewer = perReviewer,
      perDiscussion = perDiscussion,
      perRepository = perRepository,
    )
  }

  override fun getTimeSeriesTransform(date: LocalDate) = RepositoryCreatedAtTransform(
    createdAt = date,
    applyToCommentsAndFeedbacks = true,
    applyToCodeReviewsAndDiscussions = false,
  )

}
