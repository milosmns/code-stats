package stubs

import codestats.CodeReviewChange
import codestats.CodeReviewComment
import codestats.CodeReviewFeedback
import codestats.Discussion
import codestats.DiscussionComment
import codestats.Repository
import codestats.User

object DatabaseStubs {

  val repository = Repository(
    owner = "Repository owner",
    name = "Repository name",
  )

  val user = User(
    login = "octocat",
  )

  val discussion = Discussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author_login = user.login,
    created_at = "2023-04-01T15:45",
    closed_at = "2023-07-01T15:45",
    repo_owner = "Repository owner",
    repo_name = "Repository name",
  )

  val discussionComment = DiscussionComment(
    id = "1234",
    body = "Comment body",
    created_at = "2023-04-01T15:45",
    discussion_id = discussion.id,
    author_login = user.login,
    repo_owner = "Repository owner",
    repo_name = "Repository name",
  )

  val codeReview = codestats.CodeReview(
    id = 1235,
    number = 1,
    state = "OPEN",
    title = "Pull request title",
    body = "Pull request body",
    author_login = user.login,
    reviewers_csv = user.login,
    is_draft = 1,
    created_at = "2023-04-01T15:45",
    merged_at = "2023-06-01T15:45",
    closed_at = "2023-07-01T15:45",
    repo_owner = "Repository owner",
    repo_name = "Repository name",
  )

  val codeReviewComment = CodeReviewComment(
    id = 1236,
    body = "Comment body",
    created_at = "2023-04-01T15:45",
    code_review_id = codeReview.id,
    author_login = user.login,
    repo_owner = "Repository owner",
    repo_name = "Repository name",
  )

  val codeReviewChange = CodeReviewChange(
    status = "ADDED",
    additions = 1,
    deletions = 2,
    total = 3,
    file_name = "file.txt",
    code_review_id = codeReview.id,
    repo_owner = "Repository owner",
    repo_name = "Repository name",
  )

  val codeReviewFeedback = CodeReviewFeedback(
    id = 1237,
    body = "Review body",
    state = "APPROVED",
    submitted_at = "2023-05-01T15:45",
    code_review_id = codeReview.id,
    author_login = user.login,
    repo_owner = "Repository owner",
    repo_name = "Repository name",
  )

}
