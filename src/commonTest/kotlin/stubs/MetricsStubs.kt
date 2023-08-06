package stubs

import components.data.CodeReviewChange.Status.ADDED
import components.data.CodeReviewChange.Status.MODIFIED
import components.data.CodeReviewChange.Status.REMOVED
import components.data.Repository
import components.data.User
import components.metrics.GenericLongMetric
import kotlinx.datetime.LocalDateTime

object MetricsStubs {

  val user1 = User("One")
  val user2 = User("Two")
  val user3 = User("Three")
  val user4 = User("Four")

  // region Metrics
  val genericLong = object : GenericLongMetric {
    override val metricName = "Generic Long"
    override val perAuthor = mapOf(
      user1 to 1L,
      user2 to 2L,
      user3 to 3L,
    )
    override val perReviewer = mapOf(
      user1 to 10L,
      user2 to 20L,
      user3 to 30L,
    )
    override val perCodeReview = mapOf(
      Stubs.generic.codeReview.copy(number = 1) to 100L,
      Stubs.generic.codeReview.copy(number = 2) to 200L,
      Stubs.generic.codeReview.copy(number = 3) to 300L,
    )
    override val perDiscussion = mapOf(
      Stubs.generic.discussion.copy(number = 1) to 1000L,
      Stubs.generic.discussion.copy(number = 2) to 2000L,
      Stubs.generic.discussion.copy(number = 3) to 3000L,
    )
    override val perRepository = mapOf(
      Stubs.generic.repository.copy(name = "uno") to 10000L,
      Stubs.generic.repository.copy(name = "due") to 20000L,
      Stubs.generic.repository.copy(name = "tre") to 30000L,
    )
  }
  // endregion Metrics

  // region Cycle Time Repos
  val cycleTimeRepo1 = Repository(
    owner = "Repo1 owner",
    name = "Repo1 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 1,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 2, 10, 0, 0, 0),
        closedAt = null,
        author = user1,
        requestedReviewers = listOf(user2),
      ),
      Stubs.generic.codeReview.copy(
        id = 2,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 3, 10, 0, 0, 0),
        closedAt = null,
        author = user2,
        requestedReviewers = listOf(user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 3,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 4, 10, 0, 0, 0),
        closedAt = null,
        author = user1,
        requestedReviewers = listOf(user3, user4),
      ),
      Stubs.generic.codeReview.copy(
        id = 4,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = null,
        closedAt = null,
        author = user2,
        requestedReviewers = listOf(user3),
      ),
    ),
  )

  val cycleTimeRepo2 = Repository(
    owner = "Repo2 owner",
    name = "Repo2 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 5,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 1, 11, 0, 0, 0),
        closedAt = null,
        author = user4,
        requestedReviewers = listOf(user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 6,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = null,
        closedAt = LocalDateTime(2023, 4, 1, 12, 0, 0, 0),
        author = user3,
        requestedReviewers = emptyList(),
      ),
      Stubs.generic.codeReview.copy(
        id = 7,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 1, 13, 0, 0, 0),
        closedAt = null,
        author = user4,
        requestedReviewers = listOf(user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 8,
        createdAt = LocalDateTime(2023, 4, 1, 10, 0, 0, 0),
        mergedAt = LocalDateTime(2023, 4, 1, 11, 0, 0, 0),
        closedAt = null,
        author = user1,
        requestedReviewers = listOf(user3),
      ),
    ),
  )
  // endregion Cycle Time Repos

  // region Code Review Change Repos
  val codeReviewChangeRepo1 = Repository(
    owner = "Repo1 owner",
    name = "Repo1 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 1,
        author = user1,
        requestedReviewers = listOf(user2),
        changes = 10.additions + 15.mods + 20.removals,
      ),
      Stubs.generic.codeReview.copy(
        id = 2,
        author = user1,
        requestedReviewers = emptyList(),
        changes = 0.additions + 0.mods + 1.removals,
      ),
      Stubs.generic.codeReview.copy(
        id = 3,
        author = user2,
        requestedReviewers = listOf(user1),
        changes = 5.additions + 5.mods + 0.removals,
      ),
      Stubs.generic.codeReview.copy(
        id = 4,
        author = user3,
        requestedReviewers = listOf(user1, user2),
        changes = 10.additions + 0.mods + 10.removals,
      ),
    ),
  )

  val codeReviewChangeRepo2 = Repository(
    owner = "Repo2 owner",
    name = "Repo2 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 5,
        author = user3,
        requestedReviewers = listOf(user4),
        changes = 0.additions + 5.mods + 0.removals,
      ),
      Stubs.generic.codeReview.copy(
        id = 6,
        author = user1,
        requestedReviewers = listOf(user3, user4),
        changes = 10.additions + 10.mods + 10.removals,
      ),
      Stubs.generic.codeReview.copy(
        id = 7,
        author = user2,
        requestedReviewers = listOf(user4),
        changes = 0.additions + 1.mods + 0.removals,
      ),
      Stubs.generic.codeReview.copy(
        id = 8,
        author = user2,
        requestedReviewers = listOf(user4),
        changes = 1.additions + 0.mods + 0.removals,
      ),
    ),
  )
  // endregion Code Review Change Repos

  private val Int.additions
    get() = List(this) {
      Stubs.generic.codeReviewChange.copy(
        fileName = "$it.add",
        status = ADDED,
      )
    }

  private val Int.mods
    get() = List(this) {
      Stubs.generic.codeReviewChange.copy(
        fileName = "$it.mod",
        status = MODIFIED,
      )
    }

  private val Int.removals
    get() = List(this) {
      Stubs.generic.codeReviewChange.copy(
        fileName = "$it.rem",
        status = REMOVED,
      )
    }

}
