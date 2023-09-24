import commands.CommandLineArguments
import commands.CommandLineArguments.Mode.FETCH
import commands.CommandLineArguments.Mode.PRINT
import commands.CommandLineArguments.Mode.PURGE
import commands.CommandLineArguments.Mode.REPORT
import commands.CommandLineArguments.Mode.SERVE
import commands.di.provideFetchCommand
import commands.di.providePrintCommand
import commands.di.providePurgeCommand
import commands.di.provideReportCommand
import commands.di.provideServeCommand
import components.data.TeamHistoryConfig
import utils.fromFile

fun main(args: Array<String>) {
  val arguments = CommandLineArguments().load(args)
  val teamHistoryConfig = TeamHistoryConfig.fromFile(arguments.configFile)

  when (arguments.mode) {
    SERVE -> provideServeCommand(teamHistoryConfig)
    FETCH -> provideFetchCommand(teamHistoryConfig)
    REPORT -> provideReportCommand(teamHistoryConfig)
    PRINT -> providePrintCommand(teamHistoryConfig)
    PURGE -> providePurgeCommand(teamHistoryConfig)
  }.run()
}
