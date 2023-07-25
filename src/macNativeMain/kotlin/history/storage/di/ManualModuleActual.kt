package history.storage.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import codestats.codestats

actual fun provideSqlDriver(databaseLocation: String): SqlDriver =
  NativeSqliteDriver(
    schema = codestats.Schema,
    name = databaseLocation,
    onConfiguration = { config ->
      config.copy(
        extendedConfig = DatabaseConfiguration.Extended(
          foreignKeyConstraints = true,
        ),
      )
    },
  )
