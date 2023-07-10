package history.github.mapping

import com.github.graphql.GetDiscussionCommentsPageQuery
import com.github.graphql.GetDiscussionQuery
import com.github.graphql.GetDiscussionsPageQuery
import history.github.models.GitHubDiscussion
import history.github.models.GitHubUser
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val USER_UNKNOWN = GitHubUser(login = "<unknown>")

fun GetDiscussionQuery.Author.toGitHubUser(): GitHubUser =
  GitHubUser(
    login = login,
  )

fun GetDiscussionQuery.Discussion.toGitHubDiscussion(): GitHubDiscussion =
  GitHubDiscussion(
    id = id,
    number = number,
    title = title,
    body = body,
    author = author?.toGitHubUser() ?: USER_UNKNOWN,
    createdAt = Instant.parse(createdAt.toString()).toLocalDateTime(TimeZone.UTC),
    closedAt = closedAt?.let { Instant.parse(it.toString()).toLocalDateTime(TimeZone.UTC) },
    comments = emptyList(),
  )

fun GetDiscussionCommentsPageQuery.CommentAuthor.toGitHubUser(): GitHubUser =
  GitHubUser(
    login = login,
  )

fun GetDiscussionsPageQuery.Discussion.toGitHubDiscussion(): GitHubDiscussion =
  GitHubDiscussion(
    id = id,
    number = number,
    title = title,
    body = body,
    author = author?.toGitHubUser() ?: USER_UNKNOWN,
    createdAt = Instant.parse(createdAt.toString()).toLocalDateTime(TimeZone.UTC),
    closedAt = closedAt?.let { Instant.parse(it.toString()).toLocalDateTime(TimeZone.UTC) },
    comments = emptyList(),
  )

fun GetDiscussionsPageQuery.Author.toGitHubUser(): GitHubUser =
  GitHubUser(
    login = login,
  )

/**
 * Recursively converts a [GetDiscussionCommentsPageQuery.Comment] to
 * a list of [GitHubDiscussion.Comment]. Includes replies to the original comment.
 */
fun GetDiscussionCommentsPageQuery.Comment.withRepliesToGitHubComments(): List<GitHubDiscussion.Comment> =
  mutableListOf<GitHubDiscussion.Comment>()
    .also { result ->
      result += GitHubDiscussion.Comment(
        id = id,
        body = body,
        author = commentAuthor?.toGitHubUser() ?: USER_UNKNOWN,
        createdAt = Instant.parse(createdAt.toString()).toLocalDateTime(TimeZone.UTC),
      )

      replyPage.replies
        ?.mapNotNull { it?.toGitHubComment() }
        ?.let { result += it }
    }

fun GetDiscussionCommentsPageQuery.ReplyAuthor.toGitHubUser(): GitHubUser =
  GitHubUser(
    login = login,
  )

fun GetDiscussionCommentsPageQuery.Reply.toGitHubComment(): GitHubDiscussion.Comment =
  GitHubDiscussion.Comment(
    id = id,
    body = body,
    author = replyAuthor?.toGitHubUser() ?: USER_UNKNOWN,
    createdAt = Instant.parse(createdAt.toString()).toLocalDateTime(TimeZone.UTC),
  )
