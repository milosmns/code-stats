package stubs

import components.metrics.CycleTime

object MetricsStubs {

  val cycleTime = CycleTime(
    perAuthor = mapOf(
      Stubs.generic.user.copy(login = "uno") to 1,
      Stubs.generic.user.copy(login = "due") to 2,
      Stubs.generic.user.copy(login = "tre") to 3,
    ),
    perReviewer = mapOf(
      Stubs.generic.user.copy(login = "uno") to 10,
      Stubs.generic.user.copy(login = "due") to 20,
      Stubs.generic.user.copy(login = "tre") to 30,
    ),
    perCodeReview = mapOf(
      Stubs.generic.codeReview.copy(number = 1) to 100,
      Stubs.generic.codeReview.copy(number = 2) to 200,
      Stubs.generic.codeReview.copy(number = 3) to 300,
    ),
    perRepository = mapOf(
      Stubs.generic.repository.copy(name = "uno") to 1000,
      Stubs.generic.repository.copy(name = "due") to 2000,
      Stubs.generic.repository.copy(name = "tre") to 3000,
    ),
  )

}
