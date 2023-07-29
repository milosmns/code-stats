package history.github.mapping

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import stubs.Stubs

class ToGitHubMappingsTest {

  @Test fun `GraphQL user to GitHub user`() {
    val result = Stubs.gitHub.graphQl.discussionAuthor.toGitHubUser()
    val expected = Stubs.gitHub.user

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL discussion to GitHub discussion`() {
    val result = Stubs.gitHub.graphQl.discussion.toGitHubDiscussion()
    val expected = Stubs.gitHub.discussionEmpty

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL comment author to GitHub user`() {
    val result = Stubs.gitHub.graphQl.discussionCommentAuthor.toGitHubUser()
    val expected = Stubs.gitHub.user

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL comment to GitHub comments`() {
    val result = Stubs.gitHub.graphQl.discussionComment.withRepliesToGitHubComments()
    // the second comment here is actually a reply to the original comment:
    val expected = listOf(Stubs.gitHub.discussionComment, Stubs.gitHub.discussionComment.copy(id = "12340"))

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL discussion page author to GitHub user`() {
    val result = Stubs.gitHub.graphQl.discussionsPageAuthor.toGitHubUser()
    val expected = Stubs.gitHub.user

    assertThat(result).isEqualTo(expected)
  }

  @Test fun `GraphQL discussion page discussion to GitHub discussion`() {
    val result = Stubs.gitHub.graphQl.discussionsPageDiscussion.toGitHubDiscussion()
    val expected = Stubs.gitHub.discussionEmpty

    assertThat(result).isEqualTo(expected)
  }

}
