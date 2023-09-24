package server.config

import utils.readEnvVar

data class ServerConfig(
  val portApi: Int =
    readEnvVar("STATS_PORT_API")?.toIntOrNull()
      ?.takeIf { it in 1..65535 }
      ?: 8080,
)
