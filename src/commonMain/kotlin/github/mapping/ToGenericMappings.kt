package github.mapping

import github.models.GitHubDiscussion
import github.models.GitHubPullRequest
import github.models.GitHubPullRequest.File
import github.models.GitHubPullRequest.Review
import github.models.GitHubRepository
import github.models.GitHubUser
import models.CodeReview.Changes
import models.CodeReview.Feedback
import models.CodeReview as GenericCodeReview
import models.Discussion as GenericDiscussion
import models.Repository as GenericRepository
import models.User as GenericUser

fun GitHubUser.toGeneric(): GenericUser =
  GenericUser(
    login = login,
  )

fun GitHubDiscussion.Comment.toGeneric(): GenericDiscussion.Comment =
  GenericDiscussion.Comment(
    id = id,
    body = body,
    length = length,
    createdAt = createdAt,
    author = author.toGeneric(),
  )

fun GitHubDiscussion.toGeneric(): GenericDiscussion =
  GenericDiscussion(
    id = id,
    number = number,
    title = title,
    body = body,
    length = length,
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

fun File.Status.toGeneric(): Changes.Status =
  when (this) {
    File.Status.ADDED -> Changes.Status.ADDED
    File.Status.MODIFIED -> Changes.Status.MODIFIED
    File.Status.REMOVED -> Changes.Status.REMOVED
    File.Status.RENAMED -> Changes.Status.RENAMED
    File.Status.COPIED -> Changes.Status.COPIED
    File.Status.CHANGED -> Changes.Status.CHANGED
    File.Status.UNCHANGED -> Changes.Status.UNCHANGED
  }

fun File.toGeneric(): Changes =
  Changes(
    status = status.toGeneric(),
    additions = additions,
    deletions = deletions,
    total = total,
    fileName = fileName,
  )

fun Review.State.toGeneric(): Feedback.State =
  when (this) {
    Review.State.PENDING -> Feedback.State.PENDING
    Review.State.APPROVED -> Feedback.State.APPROVED
    Review.State.COMMENTED -> Feedback.State.COMMENTED
    Review.State.CHANGES_REQUESTED -> Feedback.State.CHANGES_REQUESTED
    Review.State.DISMISSED -> Feedback.State.DISMISSED
  }

fun Review.toGeneric(): Feedback =
  Feedback(
    id = id,
    body = body,
    length = length,
    state = state.toGeneric(),
    author = author.toGeneric(),
    submittedAt = submittedAt,
  )

fun GitHubPullRequest.Comment.toGeneric(): GenericCodeReview.Comment =
  GenericCodeReview.Comment(
    id = id,
    body = body,
    length = length,
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
    length = length,
    author = author.toGeneric(),
    requestedReviewers = requestedReviewers.map(GitHubUser::toGeneric),
    isDraft = isDraft,
    changedFiles = changedFiles,
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
