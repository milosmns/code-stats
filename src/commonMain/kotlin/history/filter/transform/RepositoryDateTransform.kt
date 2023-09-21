package history.filter.transform

import components.data.Repository
import history.filter.predicate.CodeReviewIsBetween
import history.filter.predicate.DiscussionIsBetween
import kotlinx.datetime.LocalDate

class RepositoryDateTransform(
  private val openDateInclusive: LocalDate,
  private val closeDateInclusive: LocalDate? = null,
) : (Repository) -> Repository {
  override fun invoke(subject: Repository) = subject.copy(
    codeReviews = subject.codeReviews
      .filter(CodeReviewIsBetween(openDateInclusive, closeDateInclusive))
      .map(CodeReviewDateTransform(openDateInclusive, closeDateInclusive)),
    discussions = subject.discussions
      .filter(DiscussionIsBetween(openDateInclusive, closeDateInclusive))
      .map(DiscussionDateTransform(openDateInclusive, closeDateInclusive)),
  )
}
