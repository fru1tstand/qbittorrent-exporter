package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.collector.maindata.AggregateTorrentCollector
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector

/** Static methods for managing collector settings. */
interface CollectorSettingsUtils {
  companion object {
    /** Maps a list of maindata collectors to their settings category. */
    val maindataCollectorSettingsMap: Map<String, Array<out MaindataCollector>> =
      mapOf(
        Pair("serverState", ServerStateCollector.values()),
        Pair("torrents", TorrentsCollector.values()),
        Pair("aggregateTorrent", AggregateTorrentCollector.values())
      )

    /** Returns the [collector]'s setting name. */
    fun getSettingsName(collector: MaindataCollector): String =
      (collector as Enum<*>).name
        .toLowerCase()
        .split("_")
        .mapIndexed { index, string -> if (index == 0) string else string.capitalize() }
        .joinToString(separator = "")

    /** Returns the map of settings with all collectors disabled. */
    fun createDefaultSettings(): Map<String, Map<String, Boolean>> {
      val result = HashMap<String, Map<String, Boolean>>()
      maindataCollectorSettingsMap.forEach { (settingsCategory, collectors) ->
        val categorySettings = HashMap<String, Boolean>()
        result[settingsCategory] = categorySettings
        collectors.forEach { collector ->
          categorySettings[getSettingsName(collector)] = false
        }
      }

      return result
    }
  }

  /**
   * Retrieves a flattened list of the enabled maindata collectors. This list will automatically
   * update when settings files are changed. One should not cache the results of this method.
   */
  fun getEnabledMaindataCollectors(): List<MaindataCollector>
}
