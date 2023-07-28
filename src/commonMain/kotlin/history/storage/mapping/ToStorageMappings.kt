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
import components.data.Discussion
import components.data.Repository
import components.data.User

fun Repository.toStorage(): DatabaseRepository =
  DatabaseRepository(
    owner = owner,
    name = name,
  )

fun User.toStorage(): DatabaseUser = DatabaseUser(login)

fun Discussion.toStorage(repoOwner: String, repoName: String): DatabaseDiscussion =
  DatabaseDiscussion(
    id = id,
    number = number.toLong(),
    title = title,
    body = body,
    created_at = createdAt.toString(),
    closed_at = closedAt?.toString(),
    author_login = author.login,
    repo_owner = repoOwner,
    repo_name = repoName,
  )

fun Discussion.Comment.toStorage(repoOwner: String, repoName: String, parentId: String): DatabaseDiscussionComment =
  DatabaseDiscussionComment(
    id = id,
    body = body,
    created_at = createdAt.toString(),
    discussion_id = parentId,
    author_login = author.login,
    repo_owner = repoOwner,
    repo_name = repoName,
  )

fun CodeReview.toStorage(repoOwner: String, repoName: String): DatabaseCodeReview =
  DatabaseCodeReview(
    id = id,
    number = number.toLong(),
    state = state.name,
    title = title,
    body = body,
    reviewers_csv = requestedReviewers.joinToString(",") { it.login },
    is_draft = if (isDraft) 1 else 0,
    changed_files = changedFiles.toLong(),
    created_at = createdAt.toString(),
    closed_at = closedAt?.toString(),
    merged_at = mergedAt?.toString(),
    author_login = author.login,
    repo_owner = repoOwner,
    repo_name = repoName,
  )

fun CodeReview.Comment.toStorage(repoOwner: String, repoName: String, parentId: Long): DatabaseCodeReviewComment =
  DatabaseCodeReviewComment(
    id = id,
    body = body,
    created_at = createdAt.toString(),
    code_review_id = parentId,
    author_login = author.login,
    repo_owner = repoOwner,
    repo_name = repoName,
  )

fun CodeReview.Change.toStorage(repoOwner: String, repoName: String, parentId: Long): DatabaseCodeReviewChange =
  DatabaseCodeReviewChange(
    status = status.name,
    additions = additions.toLong(),
    deletions = deletions.toLong(),
    total = total.toLong(),
    file_name = fileName,
    code_review_id = parentId,
    repo_owner = repoOwner,
    repo_name = repoName,
  )

fun CodeReview.Feedback.toStorage(repoOwner: String, repoName: String, parentId: Long): DatabaseCodeReviewFeedback =
  DatabaseCodeReviewFeedback(
    id = id,
    body = body,
    state = state.name,
    submitted_at = submittedAt.toString(),
    code_review_id = parentId,
    author_login = author.login,
    repo_owner = repoOwner,
    repo_name = repoName,
  )
