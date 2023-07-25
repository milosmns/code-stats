package history.storage.di

import app.cash.sqldelight.db.SqlDriver
import codestats.codestats
import history.storage.StoredHistory
import history.storage.config.StorageConfig
import kotlinx.coroutines.runBlocking
import models.TeamHistoryConfig

expect fun provideSqlDriver(databaseLocation: String): SqlDriver

fun provideStoredHistory(
  teamHistoryConfig: TeamHistoryConfig,
  storageConfig: StorageConfig = provideStorageConfig(),
) = StoredHistory(
  teamHistoryConfig = teamHistoryConfig,
  database = provideDatabase(storageConfig.databasePath),
)

fun provideStorageConfig(
  overrideDatabasePath: String? = null,
) = StorageConfig().let {
  it.copy(
    databasePath = overrideDatabasePath ?: it.databasePath,
  )
}

fun provideDatabase(databaseLocation: String) = runBlocking {
  val driver = provideSqlDriver(databaseLocation)
  codestats.Schema.create(driver).await()
  codestats(driver)
}
