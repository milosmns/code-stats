package commands.cli

import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.routing.Route

actual fun Route.setUpRoot(): Route = apply {
  singlePageApplication {
    useResources = true
    filesPath = "web"
  }
}
