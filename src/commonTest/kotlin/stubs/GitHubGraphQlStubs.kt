package stubs

import com.github.graphql.GetDiscussionCommentsPageQuery
import com.github.graphql.GetDiscussionQuery
import com.github.graphql.GetDiscussionsPageQuery

object GitHubGraphQlStubs {

  val discussionAuthor = GetDiscussionQuery.Author(
    login = "octocat",
  )

  val discussion = GetDiscussionQuery.Discussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = discussionAuthor,
    createdAt = "2023-04-01T15:45:00Z",
    closedAt = "2023-07-01T15:45:00Z",
  )

  private val replyAuthor = GetDiscussionCommentsPageQuery.ReplyAuthor(
    login = "octocat",
  )

  private val reply = GetDiscussionCommentsPageQuery.Reply(
    id = "12340",
    body = "Comment body",
    replyAuthor = replyAuthor,
    createdAt = "2023-04-01T15:45:00Z",
  )

  private val replyPage = GetDiscussionCommentsPageQuery.ReplyPage(
    replies = listOf(reply),
  )

  val discussionCommentAuthor = GetDiscussionCommentsPageQuery.CommentAuthor(
    login = "octocat",
  )

  val discussionComment = GetDiscussionCommentsPageQuery.Comment(
    id = "1234",
    body = "Comment body",
    commentAuthor = discussionCommentAuthor,
    createdAt = "2023-04-01T15:45:00Z",
    replyPage = replyPage,
  )

  val discussionsPageAuthor = GetDiscussionsPageQuery.Author(
    login = "octocat",
  )

  val discussionsPageDiscussion = GetDiscussionsPageQuery.Discussion(
    id = "123",
    number = 1,
    title = "Discussion title",
    body = "Discussion body",
    author = discussionsPageAuthor,
    createdAt = "2023-04-01T15:45:00Z",
    closedAt = "2023-07-01T15:45:00Z",
  )

}
