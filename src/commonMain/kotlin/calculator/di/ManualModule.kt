package calculator.di

import calculator.CodeReviewChangeLinesAddedCalculator
import calculator.CodeReviewChangeLinesDeletedCalculator
import calculator.CodeReviewChangeLinesTotalCalculator
import calculator.CodeReviewChangesAddedCalculator
import calculator.CodeReviewChangesModifiedCalculator
import calculator.CodeReviewChangesRemovedCalculator
import calculator.CodeReviewChangesTotalCalculator
import calculator.CodeReviewCommentsAuthoredCalculator
import calculator.CodeReviewCommentsReceivedCalculator
import calculator.CodeReviewFeedbacksApprovedCalculator
import calculator.CodeReviewFeedbacksPostponedCalculator
import calculator.CodeReviewFeedbacksRejectedCalculator
import calculator.CodeReviewFeedbacksTotalCalculator
import calculator.CodeReviewsCalculator
import calculator.CycleTimeCalculator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun provideCodeReviewChangeLinesAddedCalculator() = CodeReviewChangeLinesAddedCalculator()

fun provideCodeReviewChangeLinesDeletedCalculator() = CodeReviewChangeLinesDeletedCalculator()

fun provideCodeReviewChangeLinesTotalCalculator() = CodeReviewChangeLinesTotalCalculator()

fun provideCodeReviewChangesAddedCalculator() = CodeReviewChangesAddedCalculator()

fun provideCodeReviewChangesModifiedCalculator() = CodeReviewChangesModifiedCalculator()

fun provideCodeReviewChangesRemovedCalculator() = CodeReviewChangesRemovedCalculator()

fun provideCodeReviewChangesTotalCalculator() = CodeReviewChangesTotalCalculator()

fun provideCodeReviewCommentsAuthoredCalculator() = CodeReviewCommentsAuthoredCalculator()

fun provideCodeReviewCommentsReceivedCalculator() = CodeReviewCommentsReceivedCalculator()

fun provideCodeReviewFeedbacksApprovedCalculator() = CodeReviewFeedbacksApprovedCalculator()

fun provideCodeReviewFeedbacksPostponedCalculator() = CodeReviewFeedbacksPostponedCalculator()

fun provideCodeReviewFeedbacksRejectedCalculator() = CodeReviewFeedbacksRejectedCalculator()

fun provideCodeReviewFeedbacksTotalCalculator() = CodeReviewFeedbacksTotalCalculator()

fun provideCodeReviewsCalculator() = CodeReviewsCalculator()

fun provideCycleTimeCalculator(now: Instant = Clock.System.now()) = CycleTimeCalculator(now)

fun provideGenericLongMetricCalculators() = listOf(
  provideCodeReviewChangeLinesAddedCalculator(),
  provideCodeReviewChangeLinesDeletedCalculator(),
  provideCodeReviewChangeLinesTotalCalculator(),
  provideCodeReviewChangesAddedCalculator(),
  provideCodeReviewChangesModifiedCalculator(),
  provideCodeReviewChangesRemovedCalculator(),
  provideCodeReviewChangesTotalCalculator(),
  provideCodeReviewCommentsAuthoredCalculator(),
  provideCodeReviewCommentsReceivedCalculator(),
  provideCodeReviewFeedbacksApprovedCalculator(),
  provideCodeReviewFeedbacksPostponedCalculator(),
  provideCodeReviewFeedbacksRejectedCalculator(),
  provideCodeReviewFeedbacksTotalCalculator(),
  provideCodeReviewsCalculator(),
  provideCycleTimeCalculator(),
)
