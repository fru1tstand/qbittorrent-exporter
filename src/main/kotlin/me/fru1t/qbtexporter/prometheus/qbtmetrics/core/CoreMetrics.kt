package me.fru1t.qbtexporter.prometheus.qbtmetrics.core

import me.fru1t.qbtexporter.prometheus.exporter.MetricType

/** Exposed values from the qBittorrent web ui. */
enum class CoreMetrics(
  val settingsCategory: CoreMetricsCategories,
  val metricType: MetricType,
  val prometheusHelpString: String
) {
  ALL_TIME_DOWNLOAD_BYTES(
    CoreMetricsCategories.SERVER,
    MetricType.COUNTER,
    "The total number of downloaded bytes from all torrents (including deleted ones)."
  )
}
