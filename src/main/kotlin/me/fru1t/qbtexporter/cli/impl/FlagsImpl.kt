package me.fru1t.qbtexporter.cli.impl

import me.fru1t.qbtexporter.cli.Flags
import me.fru1t.qbtexporter.collector.CollectorSettingsUtils
import me.fru1t.qbtexporter.collector.maindata.AggregateTorrentCollector
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import me.fru1t.qbtexporter.dagger.QbtExporterComponent
import me.fru1t.qbtexporter.logger.Logger
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.annotation.Documentation
import java.util.Stack
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

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
      "-c",
      "--collectors" -> {
        logCollectors()
      }
      "-s",
      "--settings" -> {
        logSettings()
      }
      else -> {
        logHelp("Unknown flag ${args[0]}.")
      }
    }
    return true
  }

  private fun logCollectors() {
    val output = StringBuilder("\nCollectors (as they appear in settings)\n")
    output.append("  Server State\n")
    ServerStateCollector.values().forEach {
      output.append("    ${CollectorSettingsUtils.getSettingsName(it)} - ${it.help}\n")
    }
    output.append("  Torrents\n")
    TorrentsCollector.values().forEach {
      output.append("    ${CollectorSettingsUtils.getSettingsName(it)} - ${it.help}\n")
    }
    output.append("  Aggregate Torrents\n")
    AggregateTorrentCollector.values().forEach {
      output.append("    ${CollectorSettingsUtils.getSettingsName(it)} - ${it.help}\n")
    }
    logger.i(output.toString())
  }

  private fun logSettings() {
    val output = StringBuilder("\nSettings\n")
    logSettingsRecursive(Settings::class, Stack(), output)
    logger.i(output.toString())
  }

  private fun logSettingsRecursive(
    currentClass: KClass<*>,
    visitedClasses: Stack<KClass<*>>,
    output: StringBuilder
  ) {
    if (!currentClass.isData) {
      return
    }
    visitedClasses.push(currentClass)
    val outputPrefix = visitedClasses.joinToString(separator = "") { "    " }
    currentClass.primaryConstructor!!.parameters.forEach {
      output.append(
        "$outputPrefix${it.name} - ${it.findAnnotation<Documentation>()!!.documentation}\n")
      logSettingsRecursive(it.type.jvmErasure, visitedClasses, output)
    }
    visitedClasses.pop()
  }

  private fun logHelp(error: String? = null) {
    error?.let { logger.w("\n$it") }
    logger.i("\n" +
        "Usage: exporter.jar [flag]\n" +
        "[flag]\n" +
        "  <none>           Starts the exporter normally.\n" +
        "  -h|--help        Prints this help text.\n" +
        "  -c|--collectors  Outputs all collectors and their help text.\n" +
        "  -s|--settings    Outputs all settings and their documentation.\n"
    )
  }
}
