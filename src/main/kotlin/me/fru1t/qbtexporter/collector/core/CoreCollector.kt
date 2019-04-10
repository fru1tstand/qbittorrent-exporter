package me.fru1t.qbtexporter.collector.core

import me.fru1t.qbtexporter.collector.core.CoreCollectorMetricUtils.singleMetricOf
import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.MultiMetric
import me.fru1t.qbtexporter.prometheus.metric.SingleMetric
import me.fru1t.qbtexporter.qbt.response.Maindata

private object CoreCollectorMetricUtils {
  /**
   * Creates a [SingleMetric] taking the name from [coreCollector] and defaulting its value to `0`.
   */
  internal fun singleMetricOf(coreCollector: CoreCollector, help: String, metricType: MetricType) =
    SingleMetric(0, coreCollector.name.toLowerCase(), help, metricType)

  /**
   * Creates a [MultiMetric] taking the name from [coreCollector] and defaulting the metrics to an
   * empty map.
   */
  internal fun multiMetricOf(coreCollector: CoreCollector, help: String, metricType: MetricType) =
    MultiMetric(mapOf(), coreCollector.name.toLowerCase(), help, metricType)
}

/** A collector that directly translate the `maindata` qBittorrent api call to metrics. */
enum class CoreCollector(
  val category: CoreCollectorCategory,
  private val update: (Maindata, Metric) -> Unit,
  private val metric: Metric
) {
  ALL_TIME_DOWNLOAD_BYTES(
    CoreCollectorCategory.SERVER,
    { maindata, metric ->
      (metric as SingleMetric).value = maindata.serverState?.allTimeDownloadedBytes
    },
    singleMetricOf(
      ALL_TIME_DOWNLOAD_BYTES,
      "The total number of downloaded bytes from all torrents (including deleted ones) across " +
          "all sessions.",
      MetricType.COUNTER
    )
  ),

  ALL_TIME_UPLOAD_BYTES(
    CoreCollectorCategory.SERVER,
    { maindata, metric ->
      (metric as SingleMetric).value = maindata.serverState?.allTimeUploadedBytes
    },
    singleMetricOf(
      ALL_TIME_DOWNLOAD_BYTES,
      "The total number of uploaded bytes from all torrents (including deleted ones) across all " +
          "sessions.",
      MetricType.COUNTER
    )
  );

  /** Returns the help string describing what this metric represents. */
  fun getHelp(): String = metric.help

  /** Returns the [Metric] produced by this collector, given [maindata]. */
  fun collect(maindata: Maindata): Metric {
    update(maindata, metric)
    return metric
  }
}
