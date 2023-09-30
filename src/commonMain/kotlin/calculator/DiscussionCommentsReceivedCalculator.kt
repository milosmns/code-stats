package calculator

import components.data.Repository
import components.metrics.DiscussionCommentsReceived
import history.filter.transform.RepositoryCreatedAtTransform
import kotlinx.datetime.LocalDate

class DiscussionCommentsReceivedCalculator : GenericCountMetricCalculator<DiscussionCommentsReceived> {

  override fun calculate(repositories: List<Repository>): DiscussionCommentsReceived {
    @Suppress("DuplicatedCode") // false positive from CodeReviewCommentsReceivedCalculator
    val perUser = repositories
      .flatMap { repository -> repository.discussions }
      .map { discussion -> discussion.author }
      .toSet()
      .associateWith { author ->
        // we need to filter out self-comments and count only discussion comments from others
        repositories
          .flatMap { repository -> repository.discussions }
          .filter { discussion -> discussion.author == author }
          .flatMap { discussion -> discussion.comments }
          .count { comment -> comment.author != author }
          .toLong()
      }

    return DiscussionCommentsReceived(
      perAuthor = perUser,
    )
  }

  override fun getTimeSeriesTransform(date: LocalDate) = RepositoryCreatedAtTransform(
    createdAt = date,
    applyToCommentsAndFeedbacks = true,
    applyToCodeReviewsAndDiscussions = false,
  )

}
