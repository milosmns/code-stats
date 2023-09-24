package commands.di

import commands.cli.FetchCommand
import commands.cli.PrintCommand
import commands.cli.PurgeCommand
import commands.cli.ReportCommand
import commands.cli.ServeCommand
import components.data.TeamHistoryConfig
import history.TeamHistory
import history.github.di.provideGitHubHistory
import history.github.di.provideGitHubHistoryConfig
import history.storage.StoredHistory
import history.storage.config.StorageConfig
import history.storage.di.provideStorageConfig
import history.storage.di.provideStoredHistory
import server.config.ServerConfig
import server.di.provideServerConfig

fun provideServeCommand(
  teamHistoryConfig: TeamHistoryConfig,
  storedHistory: StoredHistory = provideStoredHistory(teamHistoryConfig),
  serverConfig: ServerConfig = provideServerConfig(),
) = ServeCommand(
  teamHistoryConfig = teamHistoryConfig,
  storedHistory = storedHistory,
  serverConfig = serverConfig,
)

fun providePurgeCommand(
  teamHistoryConfig: TeamHistoryConfig,
  storedHistory: StoredHistory = provideStoredHistory(teamHistoryConfig),
) = PurgeCommand(
  teamHistoryConfig = teamHistoryConfig,
  storedHistory = storedHistory,
)

fun provideFetchCommand(
  teamHistoryConfig: TeamHistoryConfig,
  teamHistory: TeamHistory = provideGitHubHistory(
    teamHistoryConfig = teamHistoryConfig,
    gitHubHistoryConfig = provideGitHubHistoryConfig(),
  ),
  storageConfig: StorageConfig = provideStorageConfig(),
  storedHistory: StoredHistory = provideStoredHistory(teamHistoryConfig),
) = FetchCommand(
  teamHistoryConfig = teamHistoryConfig,
  teamHistory = teamHistory,
  storageConfig = storageConfig,
  storedHistory = storedHistory,
)

fun providePrintCommand(
  teamHistoryConfig: TeamHistoryConfig,
  storedHistory: StoredHistory = provideStoredHistory(teamHistoryConfig),
) = PrintCommand(
  teamHistoryConfig = teamHistoryConfig,
  storedHistory = storedHistory,
)

fun provideReportCommand(
  teamHistoryConfig: TeamHistoryConfig,
  storedHistory: StoredHistory = provideStoredHistory(teamHistoryConfig),
) = ReportCommand(
  teamHistoryConfig = teamHistoryConfig,
  storedHistory = storedHistory,
)
