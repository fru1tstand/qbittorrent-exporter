package me.fru1t.qbtexporter.prometheus

import me.fru1t.qbtexporter.settings.SettingsCategory

/** Exposed values from the qBittorrent web ui. */
enum class Metric(
  val settingsCategory: SettingsCategory,
  val prometheusMetricType: PrometheusMetricType,
  val prometheusHelpString: String
) {
  ALL_TIME_DOWNLOAD_BYTES(
    SettingsCategory.SERVER,
    PrometheusMetricType.COUNTER,
    "The total number of downloaded bytes from all torrents (including deleted ones)."
  )
}
