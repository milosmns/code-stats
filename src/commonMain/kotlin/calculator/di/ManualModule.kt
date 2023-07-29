package calculator.di

import calculator.CycleTimeCalculator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun provideCycleTimeCalculator(now: Instant = Clock.System.now()) = CycleTimeCalculator(now)
