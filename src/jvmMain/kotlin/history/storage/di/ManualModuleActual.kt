package history.storage.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties

actual fun provideSqlDriver(databaseLocation: String): SqlDriver =
  JdbcSqliteDriver(
    url = "jdbc:sqlite:$databaseLocation",
    properties = Properties().apply { put("foreign_keys", "true") },
  )
