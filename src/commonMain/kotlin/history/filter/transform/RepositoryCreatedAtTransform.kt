package history.filter.transform

import components.data.Repository
import history.filter.predicate.CodeReviewIsCreatedAt
import history.filter.predicate.DiscussionIsCreatedAt
import kotlinx.datetime.LocalDate
import utils.filterIf
import utils.mapIf

class RepositoryCreatedAtTransform(
  private val createdAt: LocalDate,
  private val applyToCommentsAndFeedbacks: Boolean,
  private val applyToCodeReviewsAndDiscussions: Boolean,
) : (Repository) -> Repository {
  override fun invoke(subject: Repository) = subject.copy(
    codeReviews = subject.codeReviews
      .mapIf(applyToCommentsAndFeedbacks, CodeReviewCreatedAtTransform(createdAt))
      .filterIf(applyToCodeReviewsAndDiscussions, CodeReviewIsCreatedAt(createdAt)),
    discussions = subject.discussions
      .mapIf(applyToCommentsAndFeedbacks, DiscussionCreatedAtTransform(createdAt))
      .filterIf(applyToCodeReviewsAndDiscussions, DiscussionIsCreatedAt(createdAt)),
  )
}
