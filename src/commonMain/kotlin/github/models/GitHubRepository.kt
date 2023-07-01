package github.models

data class GitHubRepository(
  val owner: String,
  val name: String,
  val pullRequests: List<GitHubPullRequest> = emptyList(),
  val repoDiscussions: List<GitHubDiscussion> = emptyList(),
)
