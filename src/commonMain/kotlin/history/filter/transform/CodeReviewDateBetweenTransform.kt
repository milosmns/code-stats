package history.filter.transform

import components.data.CodeReview
import history.filter.predicate.CodeReviewCommentIsBetween
import history.filter.predicate.CodeReviewFeedbackIsBetween
import kotlinx.datetime.LocalDate

class CodeReviewDateBetweenTransform(
  private val openDateInclusive: LocalDate,
  private val closeDateInclusive: LocalDate? = null,
) : (CodeReview) -> CodeReview {
  override fun invoke(subject: CodeReview) = subject.copy(
    comments = subject.comments.filter(CodeReviewCommentIsBetween(openDateInclusive, closeDateInclusive)),
    feedbacks = subject.feedbacks.filter(CodeReviewFeedbackIsBetween(openDateInclusive, closeDateInclusive)),
  )
}
