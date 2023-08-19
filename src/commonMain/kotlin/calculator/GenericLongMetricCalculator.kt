package calculator

import components.data.Repository
import components.metrics.GenericCountMetric

interface GenericLongMetricCalculator<out T : GenericCountMetric> {

  fun calculate(repositories: List<Repository>): T

}
