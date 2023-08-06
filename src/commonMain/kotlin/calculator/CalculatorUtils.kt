package calculator

import components.data.CodeReview
import components.data.CodeReviewChange.Status
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import utils.epochMillisecondsUtc

// region Files
fun CodeReview.countChangesTotal(): Long = changes.size.toLong()

private val addedStatuses = setOf(Status.ADDED, Status.COPIED)
fun CodeReview.countChangesAdded(): Long = changes.count { it.status in addedStatuses }.toLong()

private val changedStatuses = setOf(Status.MODIFIED, Status.RENAMED, Status.CHANGED)
fun CodeReview.countChangesModified(): Long = changes.count { it.status in changedStatuses }.toLong()

private val removedStatuses = setOf(Status.REMOVED)
fun CodeReview.countChangesRemoved(): Long = changes.count { it.status in removedStatuses }.toLong()
// endregion Files

// region Lines
fun CodeReview.countLinesTotal(): Long = changes.sumOf { it.total }.toLong()

fun CodeReview.countLinesAdded(): Long = changes.sumOf { it.additions }.toLong()

fun CodeReview.countLinesRemoved(): Long = changes.sumOf { it.deletions }.toLong()
// endregion Lines

// region Cycle Time
fun CodeReview.getCycleTime(now: Instant = Clock.System.now()): Long = when {
  mergedAt != null -> mergedAt.epochMillisecondsUtc - createdAt.epochMillisecondsUtc
  closedAt != null -> closedAt.epochMillisecondsUtc - createdAt.epochMillisecondsUtc
  else -> now.toEpochMilliseconds() - createdAt.epochMillisecondsUtc
}
// region Cycle Time
