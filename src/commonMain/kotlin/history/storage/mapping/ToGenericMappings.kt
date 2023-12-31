package history.storage.mapping

import codestats.CodeReview as DatabaseCodeReview
import codestats.CodeReviewChange as DatabaseCodeReviewChange
import codestats.CodeReviewComment as DatabaseCodeReviewComment
import codestats.CodeReviewFeedback as DatabaseCodeReviewFeedback
import codestats.Discussion as DatabaseDiscussion
import codestats.DiscussionComment as DatabaseDiscussionComment
import codestats.Repository as DatabaseRepository
import codestats.User as DatabaseUser
import components.data.CodeReview
import components.data.CodeReviewChange
import components.data.CodeReviewComment
import components.data.CodeReviewFeedback
import components.data.Discussion
import components.data.DiscussionComment
import components.data.Repository
import components.data.User
import kotlinx.datetime.LocalDateTime

fun DatabaseRepository.toGeneric(): Repository =
  Repository(
    owner = owner,
    name = name,
    discussions = emptyList(),
    codeReviews = emptyList(),
  )

fun DatabaseUser.toGeneric(): User = User(login)

fun DatabaseDiscussion.toGeneric(): Discussion =
  Discussion(
    id = id,
    number = number.toInt(),
    title = title,
    body = body,
    createdAt = LocalDateTime.parse(created_at),
    closedAt = closed_at?.let(LocalDateTime.Companion::parse),
    author = User(author_login),
    comments = emptyList(),
  )

fun DatabaseDiscussionComment.toGeneric(): DiscussionComment =
  DiscussionComment(
    id = id,
    body = body,
    createdAt = LocalDateTime.parse(created_at),
    author = User(author_login),
  )

fun DatabaseCodeReview.toGeneric(): CodeReview =
  CodeReview(
    id = id,
    number = number.toInt(),
    state = CodeReview.State.valueOf(state),
    title = title,
    body = body,
    requestedReviewers = reviewers_csv.split(",").filter { it.isNotBlank() }.map(::User),
    isDraft = is_draft == 1L,
    createdAt = LocalDateTime.parse(created_at),
    closedAt = closed_at?.let(LocalDateTime.Companion::parse),
    mergedAt = merged_at?.let(LocalDateTime.Companion::parse),
    author = User(author_login),
    comments = emptyList(),
    changes = emptyList(),
    feedbacks = emptyList(),
  )

fun DatabaseCodeReviewComment.toGeneric(): CodeReviewComment =
  CodeReviewComment(
    id = id,
    body = body,
    createdAt = LocalDateTime.parse(created_at),
    author = User(author_login),
  )

fun DatabaseCodeReviewChange.toGeneric(): CodeReviewChange =
  CodeReviewChange(
    status = CodeReviewChange.Status.valueOf(status),
    additions = additions.toInt(),
    deletions = deletions.toInt(),
    total = total.toInt(),
    fileName = file_name,
  )

fun DatabaseCodeReviewFeedback.toGeneric(): CodeReviewFeedback =
  CodeReviewFeedback(
    id = id,
    body = body,
    state = CodeReviewFeedback.State.valueOf(state),
    submittedAt = submitted_at?.let(LocalDateTime.Companion::parse),
    author = User(author_login),
  )
