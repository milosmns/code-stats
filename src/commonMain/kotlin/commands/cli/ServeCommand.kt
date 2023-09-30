package commands.cli

import calculator.di.provideGenericCountMetricCalculators
import components.data.Repository
import components.data.TeamHistoryConfig
import components.metrics.SerializableGenericCountMetric
import history.filter.transform.RepositoryDateBetweenTransform
import history.storage.StoredHistory
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.BaseApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.Runnable
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import server.config.ServerConfig

class ServeCommand(
  private val teamHistoryConfig: TeamHistoryConfig,
  private val storedHistory: StoredHistory,
  private val serverConfig: ServerConfig,
) : Runnable {

  @Serializable
  private data class MessageResponse(val message: String)

  private lateinit var server: BaseApplicationEngine
  private lateinit var storedRepos: List<Repository>
  private val metricsByName = mutableMapOf<String, SerializableGenericCountMetric>()
  private val metricsByNameTimeSeries = mutableMapOf<String, Map<LocalDate, SerializableGenericCountMetric>>()

  override fun run() {
    println("== File configuration ==")
    println(teamHistoryConfig.simpleFormat)

    println("\n== Serving ==")
    storedRepos = storedHistory.fetchAllRepositories()
      .map {
        storedHistory.fetchRepository(
          name = it.name,
          includeCodeReviews = true,
          includeDiscussions = true,
        )
      }
      .map(RepositoryDateBetweenTransform(teamHistoryConfig.startDate, teamHistoryConfig.endDate))

    if (storedRepos.isEmpty()) {
      println("Aborted. No repositories found in the local storage.")
      return
    }

    val calculators = provideGenericCountMetricCalculators()
    calculators.forEach { calculator ->
      // store the metric for the whole time period
      val metric = calculator.calculate(storedRepos)
      metricsByName[metric.name] = metric.serializable

      // store the metric as a time series
      val timeSeries = calculator.asTimeSeries(teamHistoryConfig.startDate, teamHistoryConfig.endDate, storedRepos)
      if (timeSeries.values.isNotEmpty()) {
        metricsByNameTimeSeries[metric.name] = timeSeries.mapValues { (_, metric) -> metric.serializable }
      }
    }

    println("Now booting up a server...")
    server = embeddedServer(
      factory = CIO,
      port = serverConfig.portApi,
    ) { customizeConfiguration() }

    server.start(wait = true)
  }

  private fun Application.customizeConfiguration() {
    install(ForwardedHeaders)
    install(CORS) { allowHost("localhost") }
    install(ContentNegotiation) { json() }

    routing {
      get("/") { call.respond(MessageResponse("Yep, it runs…")) }
      get("/repos") { call.respond(storedRepos) }
      get("/metrics") { call.respond(metricsByName) }
      get("/time-series") { call.respond(metricsByNameTimeSeries) }
      get("/shutdown") {
        call.respond(MessageResponse("☠\uFE0F The server is dead now."))
        server.stop()
      }
    }
  }

}
