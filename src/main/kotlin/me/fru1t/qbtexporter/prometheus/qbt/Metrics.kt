package me.fru1t.qbtexporter.prometheus.qbt

import me.fru1t.qbtexporter.prometheus.exporter.MetricType
import me.fru1t.qbtexporter.settings.SettingsCategory

/** Exposed values from the qBittorrent web ui. */
enum class Metrics(
  val settingsCategory: SettingsCategory,
  val metricType: MetricType,
  val prometheusHelpString: String
) {
  ALL_TIME_DOWNLOAD_BYTES(
    SettingsCategory.SERVER,
    MetricType.COUNTER,
    "The total number of downloaded bytes from all torrents (including deleted ones)."
  )
}
