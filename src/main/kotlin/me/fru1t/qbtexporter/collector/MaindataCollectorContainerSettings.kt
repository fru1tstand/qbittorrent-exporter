package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings for the maindata collector containers. */
data class MaindataCollectorContainerSettings(
  @Documentation("A placeholder setting that doesn't do anything.")
  var placeholder: String? = "placeholder",

  @Documentation("Collectors for metrics per torrent.")
  var torrentsCollectors: Map<TorrentsCollector, BasicCollectorSettings>? =
    createDefaultSettingsMap { BasicCollectorSettings() }
)

private inline fun <reified E : Enum<E>, S> createDefaultSettingsMap(new: () -> S): Map<E, S> {
  val result = HashMap<E, S>()
  enumValues<E>().forEach {
    result[it] = new()
  }
  return result
}
