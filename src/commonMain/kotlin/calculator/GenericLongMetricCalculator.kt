package calculator

import components.data.Repository
import components.metrics.GenericLongMetric

interface GenericLongMetricCalculator<out T : GenericLongMetric> {

  fun calculate(repositories: List<Repository>): T

}
