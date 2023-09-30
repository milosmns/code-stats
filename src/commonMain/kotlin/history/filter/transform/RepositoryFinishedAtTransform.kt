package history.filter.transform

import components.data.Repository
import history.filter.predicate.CodeReviewIsFinishedAt
import history.filter.predicate.DiscussionIsFinishedAt
import kotlinx.datetime.LocalDate

class RepositoryFinishedAtTransform(
  private val finishedAt: LocalDate,
) : (Repository) -> Repository {
  override fun invoke(subject: Repository) = subject.copy(
    codeReviews = subject.codeReviews
      .filter(CodeReviewIsFinishedAt(finishedAt)),
    discussions = subject.discussions
      .filter(DiscussionIsFinishedAt(finishedAt)),
  )
}
