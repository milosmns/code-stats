package history.filter.transform

import components.data.Discussion
import history.filter.predicate.DiscussionCommentIsBetween
import kotlinx.datetime.LocalDate

class DiscussionDateTransform(
  private val openDateInclusive: LocalDate,
  private val closeDateInclusive: LocalDate? = null,
) : (Discussion) -> Discussion {
  override fun invoke(subject: Discussion) = subject.copy(
    comments = subject.comments.filter(DiscussionCommentIsBetween(openDateInclusive, closeDateInclusive)),
  )
}
