package me.fru1t.qbtexporter.collector.maindata

import me.fru1t.qbtexporter.collector.MaindataCollector
import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.SingleMetric
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.torrents.Torrent

/**
 * A set of collectors that transforms [Maindata.torrents] by aggregating a field into a metric.
 * This is usually accomplished by summing.
 */
enum class AggregateTorrentCollector(
  help: String,
  metricType: MetricType,
  private val update: (Collection<Torrent>) -> Number?
) : MaindataCollector {
  DOWNLOAD_REMAINING_BYTES(
    "The total number of bytes remaining to download from all torrents, including those of " +
        "unwanted files.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.downloadRemainingBytes } }
  )
  ;

  private companion object {
    private const val METRIC_NAME_PREFIX = "qbt_aggregate_torrent_"

    /**
     * Sums the output [transformation] on each [Torrent] in this collection to produce a resulting
     * aggregate value.
     */
    private fun Collection<Torrent>.sum(transformation: (Torrent) -> Long?): Long {
      var total = 0L
      forEach { total = Math.addExact(total, transformation(it) ?: 0) }
      return total
    }
  }

  private val metric: SingleMetric by lazy {
    SingleMetric(
      value = 0,
      name = METRIC_NAME_PREFIX + name.toLowerCase(),
      help = help,
      type = metricType
    )
  }

  override fun collect(maindata: Maindata): Metric {
    metric.value = update(maindata.torrents?.values ?: listOf())
    return metric
  }
}
