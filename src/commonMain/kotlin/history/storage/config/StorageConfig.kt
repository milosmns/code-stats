package history.storage.config

import utils.readEnvVar

data class StorageConfig(
  val databasePath: String =
    readEnvVar("DATABASE_PATH")
      ?.trim()?.takeIf(String::isNotEmpty)
      ?: "code-stats.db",
)
