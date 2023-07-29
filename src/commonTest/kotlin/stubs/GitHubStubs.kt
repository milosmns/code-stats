package stubs

import history.github.models.GitHubDiscussion
import history.github.models.GitHubPullRequest
import history.github.models.GitHubPullRequest.File
import history.github.models.GitHubPullRequest.Review
import history.github.models.GitHubRepository
import history.github.models.GitHubUser
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

object GitHubStubs {

  val graphQl = GitHubGraphQlStubs

  val user = GitHubUser(
    login = "octocat",
  )

  val discussionComment = GitHubDiscussion.Comment(
    id = "1234",
    body = "Comment body",
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    author = user,
  )

  val discussionEmpty = GitHubDiscussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = user,
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    closedAt = LocalDateTime(2023, 7, 1, 15, 45, 0, 0),
    comments = emptyList(),
  )

  val discussion = GitHubDiscussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = user,
    createdAt = LocalDateTime(2023, 4, 1, 15, 45, 0, 0),
    closedAt = LocalDateTime(2023, 7, 1, 15, 45, 0, 0),
    comments = listOf(discussionComment),
  )

  val pullRequestComment = GitHubPullRequest.Comment(
    id = 1236,
    body = "Comment body",
    createdAtInstant = Instant.parse("2023-04-01T15:45:00Z"),
    author = user,
  )

  val pullRequestFile = File(
    status = File.Status.ADDED,
    additions = 1,
    deletions = 2,
    total = 3,
    fileName = "file.txt",
  )

  val pullRequestReview = Review(
    id = 1237,
    body = "Review body",
    state = Review.State.APPROVED,
    submittedAtInstant = Instant.parse("2023-05-01T15:45:00Z"),
    author = user,
  )

  val pullRequest = GitHubPullRequest(
    id = 1235,
    number = 1,
    state = GitHubPullRequest.State.OPEN,
    title = "Pull request title",
    body = "Pull request body",
    author = user,
    requestedReviewers = listOf(user),
    isDraft = true,
    comments = listOf(pullRequestComment),
    files = listOf(pullRequestFile),
    reviews = listOf(pullRequestReview),
    createdAtInstant = Instant.parse("2023-04-01T15:45:00Z"),
    mergedAtInstant = Instant.parse("2023-06-01T15:45:00Z"),
    closedAtInstant = Instant.parse("2023-07-01T15:45:00Z"),
  )

  val repository = GitHubRepository(
    owner = "Repository owner",
    name = "Repository name",
    pullRequests = listOf(pullRequest),
    repoDiscussions = listOf(discussion),
  )

}
