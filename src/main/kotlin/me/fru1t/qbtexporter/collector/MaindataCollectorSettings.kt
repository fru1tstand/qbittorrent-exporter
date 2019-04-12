package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector

/** Static methods for handling */
object MaindataCollectorSettings {
  /** Maps a list of maindata collectors to their settings category. */
  private val maindataCollectorSettingsMap: Map<String, Array<out MaindataCollector>> =
      mapOf(
        Pair("serverState", ServerStateCollector.values()),
        Pair("torrents", TorrentsCollector.values())
      )

  /** Returns the map of settings with all collectors disabled. */
  fun createDefaultSettings(): Map<String, Map<String, Boolean>> {
    val result = HashMap<String, Map<String, Boolean>>()
    maindataCollectorSettingsMap.forEach { settingsCategory, collectors ->
      val categorySettings = HashMap<String, Boolean>()
      result[settingsCategory] = categorySettings
      collectors.forEach { collector ->
        categorySettings[collector.getName()] = false
      }
    }

    return result
  }

  /** Retrieves a flattened list of the enabled maindata collectors from the given [settingsMap]. */
  fun getEnabledCollectors(
    settingsMap: Map<String, Map<String, Boolean>>?
  ): List<MaindataCollector> {
    val result = ArrayList<MaindataCollector>()
    maindataCollectorSettingsMap.forEach { settingsCategory, collectors ->
      val categorySettings = settingsMap?.get(settingsCategory) ?: mapOf()
      collectors.forEach { collector ->
        if (categorySettings[collector.getName()] == true) {
          result.add(collector)
        }
      }
    }
    return result
  }
}
