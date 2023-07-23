package history.storage.config

import readEnvVar

data class StorageConfig(
  val databasePath: String =
    readEnvVar("DATABASE_PATH")
      ?.trim()?.takeIf(String::isNotEmpty)
      ?: "code-stats.db",
)
