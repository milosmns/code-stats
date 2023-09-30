package history.filter.transform

import components.data.Discussion
import history.filter.predicate.DiscussionCommentIsCreatedAt
import kotlinx.datetime.LocalDate

class DiscussionCreatedAtTransform(
  private val createdAt: LocalDate,
) : (Discussion) -> Discussion {
  override fun invoke(subject: Discussion) = subject.copy(
    comments = subject.comments.filter(DiscussionCommentIsCreatedAt(createdAt)),
  )
}
