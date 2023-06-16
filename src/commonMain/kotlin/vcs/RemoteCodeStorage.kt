package vcs

import models.Repository

interface RemoteCodeStorage {

  fun fetchRepo(repo: String): Repository

}
