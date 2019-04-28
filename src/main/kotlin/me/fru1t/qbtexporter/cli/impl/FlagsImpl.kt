package me.fru1t.qbtexporter.cli.impl

import me.fru1t.qbtexporter.cli.Flags
import me.fru1t.qbtexporter.dagger.QbtExporterComponent
import me.fru1t.qbtexporter.logger.Logger
import javax.inject.Inject
import javax.inject.Named

/** Implementation of [Flags]. */
class FlagsImpl @Inject constructor(
  @Named(QbtExporterComponent.CLI_ARGS_NAME) private val args: Array<String>,
  private val logger: Logger
) : Flags {
  override fun handle(): Boolean {
    if (args.isEmpty()) {
      return false
    }

    if (args.size > 1) {
      logHelp("Only a single flag can be passed in at a time. Found ${args.size}.")
      return true
    }

    when (args[0]) {
      "-h",
      "--help" -> {
        logHelp()
      }
      else -> {
        logHelp("Unknown flag ${args[0]}.")
      }
    }
    return true
  }

  private fun logHelp(error: String? = null) {
    error?.let { logger.w("\n$it") }
    logger.i("\n" +
        "Usage: exporter.jar [flag]\n" +
        "[flag]\n" +
        "  <none>       Starts the exporter normally.\n" +
        "  -h|--help    Prints this help text.\n"
    )
  }
}