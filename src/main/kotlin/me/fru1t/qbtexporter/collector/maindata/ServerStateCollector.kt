package me.fru1t.qbtexporter.collector.maindata

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.SingleMetric
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.ServerState

/** A set of collectors that transform [Maindata.serverState] fields into metrics. */
enum class ServerStateCollector(
  help: String,
  metricType: MetricType,
  private val update: (ServerState?) -> Number?
) {
  ALL_TIME_DOWNLOAD_BYTES(
    "The total number of downloaded bytes from all torrents (including deleted ones) across " +
        "all sessions.",
    MetricType.COUNTER,
    { serverState -> serverState?.allTimeDownloadedBytes }
  ),

  ALL_TIME_UPLOAD_BYTES(
    "The total number of uploaded bytes from all torrents (including deleted ones) across all " +
        "sessions.",
    MetricType.COUNTER,
    { serverState -> serverState?.allTimeUploadedBytes }
  );

  private companion object {
    private const val METRIC_NAME_PREFIX = "qbt_server_state_"
  }

  private val metric: SingleMetric by lazy {
    SingleMetric(
      value = 0,
      name = METRIC_NAME_PREFIX + name.toLowerCase(),
      help = help,
      type = metricType
    )
  }

  /** Returns the [Metric] produced by this collector, given [maindata]. */
  fun collect(maindata: Maindata): Metric {
    metric.value = update(maindata.serverState)
    return metric
  }
}
