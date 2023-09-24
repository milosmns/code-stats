package commands.cli

import components.data.Repository
import components.data.TeamHistoryConfig
import history.storage.StoredHistory
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.BaseApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.Runnable
import server.config.ServerConfig

class ServeCommand(
  private val teamHistoryConfig: TeamHistoryConfig,
  private val storedHistory: StoredHistory,
  private val serverConfig: ServerConfig,
) : Runnable {

  private lateinit var server: BaseApplicationEngine
  private lateinit var storedRepos: List<Repository>

  override fun run() {
    println("== File configuration ==")
    println(teamHistoryConfig.simpleFormat)

    println("\n== Serving ==")
    storedRepos = storedHistory.fetchAllRepositories().map {
      storedHistory.fetchRepository(
        name = it.name,
        includeCodeReviews = true,
        includeDiscussions = true,
      )
    }
    if (storedRepos.isEmpty()) {
      println("Aborted. No repositories found in the local storage.")
      return
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
      get("/") { respondToRoot() }
      get("/shutdown") { respondToShutdown() }
    }
  }

  private suspend fun PipelineContext<Unit, ApplicationCall>.respondToRoot() {
    call.respond(storedRepos)
  }

  private suspend fun PipelineContext<Unit, ApplicationCall>.respondToShutdown() {
    call.respondText("☠\uFE0F The server is dead now.")
    server.stop()
  }

}
