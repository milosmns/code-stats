package calculator

import components.data.Repository
import components.metrics.GenericCountMetric
import kotlinx.datetime.LocalDate

interface GenericCountMetricCalculator<out T : GenericCountMetric> {

  fun calculate(repositories: List<Repository>): T

  fun getTimeSeriesTransform(date: LocalDate): (Repository) -> Repository

  fun asTimeSeries(
    startDate: LocalDate,
    endDate: LocalDate,
    repositories: List<Repository>,
  ): Map<LocalDate, T> {
    val results = mutableMapOf<LocalDate, T>()
    for (date in startDate..endDate step 1) {
      val scopedDownRepos = repositories
        .map(getTimeSeriesTransform(date))
        .filterNot(Repository::isDead)
      if (scopedDownRepos.isEmpty()) continue
      results[date] = calculate(scopedDownRepos)
    }
    return results
  }

}
