package stubs

import components.data.CodeReviewChange
import components.data.CodeReviewChange.Status.ADDED
import components.data.CodeReviewChange.Status.MODIFIED
import components.data.CodeReviewChange.Status.REMOVED
import components.data.CodeReviewFeedback.State
import components.data.CodeReviewFeedback.State.APPROVED
import components.data.CodeReviewFeedback.State.CHANGES_REQUESTED
import components.data.Repository
import components.data.User
import components.metrics.GenericCountMetric
import kotlinx.datetime.LocalDateTime

object MetricsStubs {

  val user1 = User("One")
  val user2 = User("Two")
  val user3 = User("Three")
  val user4 = User("Four")

  // region Metrics
  val genericLong = object : GenericCountMetric {
    override val name = "Generic Long"
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

  // region Code Review Comment Repos
  val codeReviewCommentRepo1 = Repository(
    owner = "Repo1 owner",
    name = "Repo1 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 1,
        author = user1,
        requestedReviewers = listOf(user2),
        comments = 4.codeComments(from = user1),
      ),
      Stubs.generic.codeReview.copy(
        id = 2,
        author = user1,
        requestedReviewers = listOf(user2, user3),
        comments = 2.codeComments(from = user1) + 1.codeComments(from = user2),
      ),
      Stubs.generic.codeReview.copy(
        id = 3,
        author = user2,
        requestedReviewers = listOf(user1),
        comments = 2.codeComments(from = user2) + 1.codeComments(from = user3),
      ),
    ),
  )

  val codeReviewCommentRepo2 = Repository(
    owner = "Repo2 owner",
    name = "Repo2 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 4,
        author = user1,
        requestedReviewers = listOf(user2),
        comments = 1.codeComments(from = user1) + 1.codeComments(from = user2),
      ),
      Stubs.generic.codeReview.copy(
        id = 5,
        author = user3,
        requestedReviewers = listOf(user1),
        comments = 4.codeComments(from = user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 6,
        author = user2,
        requestedReviewers = listOf(user3),
        comments = 3.codeComments(from = user2),
      ),
    ),
  )
  // endregion Code Review Comment Repos

  // region Code Review Line Repos
  val codeReviewLineRepo1 = Repository(
    owner = "Repo1 owner",
    name = "Repo1 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 1,
        author = user1,
        requestedReviewers = listOf(user2),
        changes = (2.additions + 2.mods + 2.removals) // irrelevant distribution for line stats
          .withLinesAdded(30)
          .withLinesDeleted(10),
      ),
      Stubs.generic.codeReview.copy(
        id = 2,
        author = user1,
        requestedReviewers = emptyList(),
        changes = (0.additions + 0.mods + 1.removals) // irrelevant distribution for line stats
          .withLinesAdded(0)
          .withLinesDeleted(1),
      ),
      Stubs.generic.codeReview.copy(
        id = 3,
        author = user2,
        requestedReviewers = listOf(user1, user3),
        changes = (1.additions + 0.mods + 0.removals) // irrelevant distribution for line stats
          .withLinesAdded(1)
          .withLinesDeleted(0),
      ),
    ),
  )

  val codeReviewLineRepo2 = Repository(
    owner = "Repo2 owner",
    name = "Repo2 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 4,
        author = user2,
        requestedReviewers = listOf(user1, user3),
        changes = (1.additions + 2.mods + 3.removals) // irrelevant distribution for line stats
          .withLinesAdded(5)
          .withLinesDeleted(5),
      ),
      Stubs.generic.codeReview.copy(
        id = 5,
        author = user2,
        requestedReviewers = listOf(user3),
        changes = (3.additions + 2.mods + 1.removals) // irrelevant distribution for line stats
          .withLinesAdded(0)
          .withLinesDeleted(10),
      ),
      Stubs.generic.codeReview.copy(
        id = 6,
        author = user1,
        requestedReviewers = listOf(user3),
        changes = (10.additions + 50.mods + 100.removals) // irrelevant distribution for line stats
          .withLinesAdded(5)
          .withLinesDeleted(5),
      ),
    ),
  )
  // endregion Code Review Line Repos

  // region Code Review Feedback Repos
  val codeReviewFeedbackRepo1 = Repository(
    owner = "Repo1 owner",
    name = "Repo1 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 1,
        author = user1,
        requestedReviewers = listOf(user3),
        feedbacks = 10.approvals(from = user3) + 2.rejections(from = user3) + 2.postponements(from = user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 2,
        author = user1,
        requestedReviewers = listOf(user3),
        feedbacks = 3.approvals(from = user3) + 0.rejections(from = user3) + 0.postponements(from = user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 3,
        author = user2,
        requestedReviewers = listOf(user1, user3),
        feedbacks = 0.approvals(from = user1) + 0.rejections(from = user1) + 0.postponements(from = user3),
      ),
    ),
  )

  val codeReviewFeedbackRepo2 = Repository(
    owner = "Repo2 owner",
    name = "Repo2 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 4,
        author = user2,
        requestedReviewers = listOf(user3),
        feedbacks = 0.approvals(from = user3) + 1.rejections(from = user3) + 0.postponements(from = user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 5,
        author = user2,
        requestedReviewers = listOf(user3),
        feedbacks = 10.approvals(from = user3) + 0.rejections(from = user3) + 0.postponements(from = user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 6,
        author = user2,
        requestedReviewers = listOf(user3),
        feedbacks = 11.approvals(from = user3) + 0.rejections(from = user3) + 3.postponements(from = user3),
      ),
    ),
  )
  // endregion Code Review Feedback Repos

  // region Code Reviews Totals Repos
  val codeReviewsTotalsRepo1 = Repository(
    owner = "Repo1 owner",
    name = "Repo1 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 1,
        author = user1,
        requestedReviewers = emptyList(),
      ),
      Stubs.generic.codeReview.copy(
        id = 2,
        author = user2,
        requestedReviewers = listOf(user1),
      ),
      Stubs.generic.codeReview.copy(
        id = 3,
        author = user2,
        requestedReviewers = listOf(user1, user3),
      ),
      Stubs.generic.codeReview.copy(
        id = 4,
        author = user4,
        requestedReviewers = listOf(user1),
      ),
    ),
  )

  val codeReviewsTotalsRepo2 = Repository(
    owner = "Repo2 owner",
    name = "Repo2 name",
    discussions = emptyList(),
    codeReviews = listOf(
      Stubs.generic.codeReview.copy(
        id = 5,
        author = user4,
        requestedReviewers = listOf(user2),
      ),
      Stubs.generic.codeReview.copy(
        id = 6,
        author = user3,
        requestedReviewers = listOf(user2),
      ),
    ),
  )
  // endregion Code Reviews Totals Repos

  // region Discussion Comments Repos
  val discussionCommentsRepo1 = Repository(
    owner = "Repo1 owner",
    name = "Repo1 name",
    codeReviews = emptyList(),
    discussions = listOf(
      Stubs.generic.discussion.copy(
        id = "1",
        number = 1,
        author = user1,
        comments = 4.discussionComments(from = user3) + 2.discussionComments(from = user1),
      ),
      Stubs.generic.discussion.copy(
        id = "2",
        number = 2,
        author = user3,
        comments = emptyList(),
      ),
    ),
  )

  val discussionCommentsRepo2 = Repository(
    owner = "Repo2 owner",
    name = "Repo2 name",
    codeReviews = emptyList(),
    discussions = listOf(
      Stubs.generic.discussion.copy(
        id = "3",
        number = 3,
        author = user2,
        comments = 2.discussionComments(from = user3) + 4.discussionComments(from = user1),
      ),
      Stubs.generic.discussion.copy(
        id = "4",
        number = 4,
        author = user3,
        comments = 3.discussionComments(from = user3) + 1.discussionComments(from = user2),
      ),
      Stubs.generic.discussion.copy(
        id = "5",
        number = 5,
        author = user2,
        comments = 2.discussionComments(from = user3) + 1.discussionComments(from = user2),
      ),
    ),
  )
  // endregion Discussion Comments Repos

  // region Utils
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

  private fun Int.codeComments(from: User) = List(this) {
    Stubs.generic.codeReviewComment.copy(author = from)
  }

  // distributes the lines across files; the remainder from the division is added the first files
  // example: 11 lines across 3 files -> 4, 4, 3
  // example: 10 lines across 2 files -> 5, 5
  private fun List<CodeReviewChange>.withLinesAdded(lines: Int) = mapIndexed { index, change ->
    val distributionRemainder = if (index < lines % size) 1 else 0
    val updatedAdditions = lines / size + distributionRemainder
    change.copy(
      additions = updatedAdditions,
      total = change.total - change.additions + updatedAdditions,
    )
  }

  // same as above, but for deletions
  private fun List<CodeReviewChange>.withLinesDeleted(lines: Int) = mapIndexed { index, change ->
    val distributionRemainder = if (index < lines % size) 1 else 0
    val updatedDeletions = lines / size + distributionRemainder
    change.copy(
      deletions = updatedDeletions,
      total = change.total - change.deletions + updatedDeletions,
    )
  }

  private fun Int.approvals(from: User = Stubs.generic.user) = List(this) {
    Stubs.generic.codeReviewFeedback.copy(
      author = from,
      state = APPROVED,
    )
  }

  private fun Int.rejections(from: User = Stubs.generic.user) = List(this) {
    Stubs.generic.codeReviewFeedback.copy(
      author = from,
      state = CHANGES_REQUESTED,
    )
  }

  private fun Int.postponements(from: User = Stubs.generic.user) = List(this) {
    Stubs.generic.codeReviewFeedback.copy(
      author = from,
      state = State.entries
        .filterNot { it in setOf(APPROVED, CHANGES_REQUESTED) }
        .random(),
    )
  }

  private fun Int.discussionComments(from: User) = List(this) {
    Stubs.generic.discussionComment.copy(author = from)
  }
  // endregion Utils

}
