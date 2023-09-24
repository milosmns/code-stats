package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import commands.CommandLineArguments.Mode.REPORT

class CommandLineArguments : CliktCommand(
  name = "codestats",
  help = "Code Stats Utility. Run with --help for more.",
) {

  enum class Mode { SERVE, FETCH, REPORT, PRINT, PURGE }

  val mode: Mode by option()
    .enum<Mode>(ignoreCase = true) { it.name.lowercase() }
    .default(REPORT)
    .help(
      listOf(
        "[Serve] launches a server and prints the access URL",
        "[Fetch] fetches fresh git data and overwrites the stored history",
        "[Report] a short report on what data is available",
        "[Print] calculates and prints the code stats to stdout",
        "[Purge] deletes all previously stored data",
      ).joinToString(". ")
    )

  val configFile: String by option()
    .default("src/commonMain/resources/sample.config.yaml")
    .help("Path to the configuration YAML file")

  override fun run() = Unit

  fun load(args: Array<String>) = apply { main(args) }

}
