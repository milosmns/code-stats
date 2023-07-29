package stubs

import components.data.CodeReview
import components.data.CodeReview.Change
import components.data.CodeReview.Feedback
import components.data.Discussion
import components.data.Repository
import components.data.User
import kotlinx.datetime.LocalDateTime

object GenericStubs {

  val user = User(
    login = "octocat",
  )

  val discussionComment = Discussion.Comment(
    id = "1234",
    body = "Comment body",
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    author = user,
  )

  val discussionEmpty = Discussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = user,
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    closedAt = LocalDateTime(2023, 7, 1, 15, 45, 0, 0),
    comments = emptyList(),
  )

  val discussion = Discussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = user,
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    closedAt = LocalDateTime(2023, 7, 1, 15, 45, 0, 0),
    comments = listOf(discussionComment),
  )

  val codeReviewComment = CodeReview.Comment(
    id = 1236,
    body = "Comment body",
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    author = user,
  )

  val codeReviewChange = Change(
    status = Change.Status.ADDED,
    additions = 1,
    deletions = 2,
    total = 3,
    fileName = "file.txt",
  )

  val codeReviewFeedback = Feedback(
    id = 1237,
    body = "Review body",
    state = Feedback.State.APPROVED,
    submittedAt = LocalDateTime(2023, 5, 1, 15, 45, 0, 0),
    author = user,
  )

  val codeReviewEmpty = CodeReview(
    id = 1235,
    number = 1,
    state = CodeReview.State.OPEN,
    title = "Pull request title",
    body = "Pull request body",
    author = user,
    requestedReviewers = listOf(user),
    isDraft = true,
    comments = emptyList(),
    changes = emptyList(),
    feedbacks = emptyList(),
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    mergedAt = LocalDateTime(2023, 6, 1, 15, 45, 0, 0),
    closedAt = LocalDateTime(2023, 7, 1, 15, 45, 0, 0),
  )

  val codeReview = CodeReview(
    id = 1235,
    number = 1,
    state = CodeReview.State.OPEN,
    title = "Pull request title",
    body = "Pull request body",
    author = user,
    requestedReviewers = listOf(user),
    isDraft = true,
    comments = listOf(codeReviewComment),
    changes = listOf(codeReviewChange),
    feedbacks = listOf(codeReviewFeedback),
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    mergedAt = LocalDateTime(2023, 6, 1, 15, 45, 0, 0),
    closedAt = LocalDateTime(2023, 7, 1, 15, 45, 0, 0),
  )

  val repositoryEmpty = Repository(
    owner = "Repository owner",
    name = "Repository name",
    discussions = emptyList(),
    codeReviews = emptyList(),
  )

  val repository = Repository(
    owner = "Repository owner",
    name = "Repository name",
    discussions = listOf(discussion),
    codeReviews = listOf(codeReview),
  )

}
