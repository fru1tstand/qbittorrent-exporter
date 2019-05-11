package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.collector.maindata.AggregateTorrentCollector
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings for the maindata collector containers. */
data class MaindataCollectorContainerSettings(
  @Documentation("Collectors for metrics per torrent.")
  var torrentsCollectors: Map<TorrentsCollector, BasicCollectorSettings>? =
    createDefaultSettingsMap { BasicCollectorSettings() },

  @Documentation("Collectors for maindata metrics.")
  var serverStateCollectors: Map<ServerStateCollector, BasicCollectorSettings>? =
    createDefaultSettingsMap { BasicCollectorSettings() },

  @Documentation("Collectors for aggregate torrent metrics.")
  var aggregateTorrentCollectors: Map<AggregateTorrentCollector, BasicCollectorSettings>? =
    createDefaultSettingsMap { BasicCollectorSettings() }
)

private inline fun <reified E : Enum<E>, S> createDefaultSettingsMap(new: () -> S): Map<E, S> {
  val result = HashMap<E, S>()
  enumValues<E>().forEach {
    result[it] = new()
  }
  return result
}
