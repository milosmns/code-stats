package history.github.mapping

import components.data.CodeReview as GenericCodeReview
import components.data.Discussion as GenericDiscussion
import components.data.Repository as GenericRepository
import components.data.User as GenericUser
import components.data.CodeReviewChange
import components.data.CodeReviewFeedback
import components.data.CodeReviewComment
import components.data.DiscussionComment
import history.github.models.GitHubDiscussion
import history.github.models.GitHubPullRequest
import history.github.models.GitHubPullRequest.File
import history.github.models.GitHubPullRequest.Review
import history.github.models.GitHubRepository
import history.github.models.GitHubUser

fun GitHubUser.toGeneric(): GenericUser =
  GenericUser(
    login = login,
  )

fun GitHubDiscussion.Comment.toGeneric(): DiscussionComment =
  DiscussionComment(
    id = id,
    body = body,
    createdAt = createdAt,
    author = author.toGeneric(),
  )

fun GitHubDiscussion.toGeneric(): GenericDiscussion =
  GenericDiscussion(
    id = id,
    number = number,
    title = title,
    body = body,
    author = author.toGeneric(),
    createdAt = createdAt,
    closedAt = closedAt,
    comments = comments.map(GitHubDiscussion.Comment::toGeneric),
  )

fun GitHubPullRequest.State.toGeneric(): GenericCodeReview.State =
  when (this) {
    GitHubPullRequest.State.OPEN -> GenericCodeReview.State.OPEN
    GitHubPullRequest.State.CLOSED -> GenericCodeReview.State.CLOSED
  }

fun File.Status.toGeneric(): CodeReviewChange.Status =
  when (this) {
    File.Status.ADDED -> CodeReviewChange.Status.ADDED
    File.Status.MODIFIED -> CodeReviewChange.Status.MODIFIED
    File.Status.REMOVED -> CodeReviewChange.Status.REMOVED
    File.Status.RENAMED -> CodeReviewChange.Status.RENAMED
    File.Status.COPIED -> CodeReviewChange.Status.COPIED
    File.Status.CHANGED -> CodeReviewChange.Status.CHANGED
    File.Status.UNCHANGED -> CodeReviewChange.Status.UNCHANGED
  }

fun File.toGeneric(): CodeReviewChange =
  CodeReviewChange(
    status = status.toGeneric(),
    additions = additions,
    deletions = deletions,
    total = total,
    fileName = fileName,
  )

fun Review.State.toGeneric(): CodeReviewFeedback.State =
  when (this) {
    Review.State.PENDING -> CodeReviewFeedback.State.PENDING
    Review.State.APPROVED -> CodeReviewFeedback.State.APPROVED
    Review.State.COMMENTED -> CodeReviewFeedback.State.COMMENTED
    Review.State.CHANGES_REQUESTED -> CodeReviewFeedback.State.CHANGES_REQUESTED
    Review.State.DISMISSED -> CodeReviewFeedback.State.DISMISSED
  }

fun Review.toGeneric(): CodeReviewFeedback =
  CodeReviewFeedback(
    id = id,
    body = body,
    state = state.toGeneric(),
    author = author.toGeneric(),
    submittedAt = submittedAt,
  )

fun GitHubPullRequest.Comment.toGeneric(): CodeReviewComment =
  CodeReviewComment(
    id = id,
    body = body,
    createdAt = createdAt,
    author = author.toGeneric(),
  )

fun GitHubPullRequest.toGeneric(): GenericCodeReview =
  GenericCodeReview(
    id = id,
    number = number,
    state = state.toGeneric(),
    title = title,
    body = body,
    author = author.toGeneric(),
    requestedReviewers = requestedReviewers.map(GitHubUser::toGeneric),
    isDraft = isDraft,
    comments = comments.map(GitHubPullRequest.Comment::toGeneric),
    changes = files.map(File::toGeneric),
    feedbacks = reviews.map(Review::toGeneric),
    createdAt = createdAt,
    closedAt = closedAt,
    mergedAt = mergedAt,
  )

fun GitHubRepository.toGeneric(): GenericRepository =
  GenericRepository(
    owner = owner,
    name = name,
    codeReviews = pullRequests.map(GitHubPullRequest::toGeneric),
    discussions = repoDiscussions.map(GitHubDiscussion::toGeneric),
  )
