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
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.Runnable
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import server.config.ServerConfig

class ServeCommand(
  private val teamHistoryConfig: TeamHistoryConfig,
  private val storedHistory: StoredHistory,
  private val serverConfig: ServerConfig,
) : Runnable {

  @Serializable
  data class MessageResponse(val message: String)

  private lateinit var server: BaseApplicationEngine
  private lateinit var storedRepos: List<Repository>
  private val metricsByName = mutableMapOf<String, SerializableGenericCountMetric>()
  private val metricsByNameTimeSeries = mutableMapOf<String, Map<LocalDate, SerializableGenericCountMetric>>()

  override fun run() {
    println("== File configuration ==")
    println(teamHistoryConfig.simpleFormat)

    println("\n== Serving ==")
    println("Searching local storage...")
    storedRepos = storedHistory.fetchAllRepositories()
      .filter {
        teamHistoryConfig.teams
          .flatMap { team -> team.codeRepositories + team.discussionRepositories }
          .contains(it.name)
      }
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

    println("Now booting up the server...")
    server = embeddedServer(
      factory = CIO,
      port = serverConfig.portApi,
    ) { setUp() }

    server.start(wait = true)
  }

  private fun Application.setUp() {
    install(ForwardedHeaders)
    install(CORS) { anyHost() }
    install(ContentNegotiation) { json() }

    routing {
      setUpRoot()

      // lists stored repos
      get("/api/repos") { call.respond(storedRepos) }

      // lists available metrics
      get("/api/metrics") { call.respond(metricsByName.keys.sorted()) }

      // lists the metric values (count/duration/etc.)
      get("/api/counts/{metric}") {
        val metric = call.parameters["metric"]?.trim()
        val response = if (metric.isNullOrBlank()) {
          metricsByName
        } else {
          val emptyJson = emptyMap<String, String>()
          metricsByName[metric] ?: emptyJson
        }
        call.respond(response.toJsonElement())
      }

      // lists the metric values (count/duration/etc.) as a time series, date-based
      get("/api/time-series/{metric}") {
        val metric = call.parameters["metric"]?.trim()
        val response = if (metric.isNullOrBlank()) {
          metricsByNameTimeSeries
        } else {
          val emptyJson = emptyMap<String, String>()
          metricsByNameTimeSeries[metric] ?: emptyJson
        }
        call.respond(response.toJsonElement())
      }

      // shuts the server down
      get("/api/shutdown") {
        call.respond(MessageResponse("☠\uFE0F The server is dead now."))
        server.stop()
      }
    }
  }

  // Serialization hacks…

  private fun Any?.toJsonElement(): JsonElement =
    when (this) {
      null -> JsonNull
      is Map<*, *> -> toJsonElement()
      is Collection<*> -> toJsonElement()
      is Boolean -> JsonPrimitive(this)
      is Number -> JsonPrimitive(this)
      is String -> JsonPrimitive(this)
      is Enum<*> -> JsonPrimitive(this.toString())
      is SerializableGenericCountMetric -> Json.encodeToJsonElement(this)
      else -> throw IllegalStateException("Can't serialize unknown type: $this")
    }

  private fun <T> Collection<T>.toJsonElement(): JsonElement {
    val list: MutableList<JsonElement> = mutableListOf()
    this.forEach { value ->
      when (value) {
        null -> list.add(JsonNull)
        is Map<*, *> -> list.add(value.toJsonElement())
        is Collection<*> -> list.add(value.toJsonElement())
        is Boolean -> list.add(JsonPrimitive(value))
        is Number -> list.add(JsonPrimitive(value))
        is String -> list.add(JsonPrimitive(value))
        is Enum<*> -> list.add(JsonPrimitive(value.toString()))
        is SerializableGenericCountMetric -> list.add(value.toJsonElement())
        else -> throw IllegalStateException("Can't serialize unknown collection type: $value")
      }
    }
    return JsonArray(list)
  }

  private fun <K, V> Map<K, V>.toJsonElement(): JsonElement {
    val map: MutableMap<String, JsonElement> = mutableMapOf()
    this.forEach { (typedKey, value) ->
      val key = typedKey.toString()
      when (value) {
        null -> map[key] = JsonNull
        is Map<*, *> -> map[key] = value.toJsonElement()
        is Collection<*> -> map[key] = value.toJsonElement()
        is Boolean -> map[key] = JsonPrimitive(value)
        is Number -> map[key] = JsonPrimitive(value)
        is String -> map[key] = JsonPrimitive(value)
        is Enum<*> -> map[key] = JsonPrimitive(value.toString())
        is SerializableGenericCountMetric -> map[key] = value.toJsonElement()
        else -> throw IllegalStateException("Can't serialize unknown type: $value")
      }
    }
    return JsonObject(map)
  }

}

expect fun Route.setUpRoot(): Route
