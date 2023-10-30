package commands.cli

import commands.cli.ServeCommand.MessageResponse
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

actual fun Route.setUpRoot() = get("/") {
  call.respond(MessageResponse("UI runs only on the JVM"))
}
