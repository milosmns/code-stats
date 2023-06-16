package github

import vcs.RemoteCodeStorage
import models.Repository

class GitHubApi : RemoteCodeStorage {

  override fun fetchRepo(repo: String): Repository {
    throw IllegalArgumentException()
  }

}
