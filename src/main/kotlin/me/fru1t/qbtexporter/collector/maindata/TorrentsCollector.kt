package me.fru1t.qbtexporter.collector.maindata

import me.fru1t.qbtexporter.collector.MaindataCollectorContainer
import me.fru1t.qbtexporter.collector.settings.MaindataCollectorContainerSettings
import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.MultiMetric
import me.fru1t.qbtexporter.prometheus.metric.multimetric.MetricLabel
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.torrents.Torrent

/** A set of collectors that transforms [Maindata.torrents] into labelled metrics per torrent. */
enum class TorrentsCollector(
  val help: String,
  metricType: MetricType,
  private val update: (Map<String, Torrent>) -> Map<MetricLabel, Number?>
) {
  DOWNLOAD_REMAINING_BYTES(
    "The amount of bytes remaining to download, including those of unwanted files.",
    MetricType.GAUGE,
    { torrents -> torrents.mapNonZeroToMetric { it.downloadRemainingBytes } }
  ),
  COMPLETED_BYTES(
    "The amount of 'actual' bytes completed from any source. 'Actual', meaning, blocks of data " +
        "that have passed CRC and are verified to be non-corrupt. Any source as it's possible " +
        "that a torrent could be pieced together from sources other than that of qbt.",
    MetricType.GAUGE,
    { torrents -> torrents.mapAllToMetric { it.completedBytes } }
  ),
  DOWNLOAD_PAYLOAD_RATE_BYTES_PER_SECOND(
    "The download rate of the torrent's payload only (ie. doesn't include protocol chatter) " +
        "in bytes per second.",
    MetricType.GAUGE,
    { torrents -> torrents.mapNonZeroToMetric { it.downloadPayloadRateBytesPerSecond } }
  ),
  DOWNLOAD_TOTAL_BYTES(
    "The number of downloaded bytes across all sessions including wasted data.",
    MetricType.COUNTER,
    { torrents -> torrents.mapAllToMetric { it.downloadTotalBytes } }
  ),
  DOWNLOAD_SESSION_BYTES(
    "The number of downloaded bytes during the current session including wasted data. A session " +
        "is defined by torrent lifecycle (ie. when a torrent is stopped, its session is ended).",
    MetricType.COUNTER,
    { torrents -> torrents.mapNonZeroToMetric { it.downloadSessionBytes } }
  ),
  SEEDERS_AVAILABLE(
    "The number of seeders seeding this torrent. A tracker's announce for the number of seeders " +
        "takes precedence for this value; otherwise qBt will take a best guess by polling the " +
        "swarm.",
    MetricType.GAUGE,
    { torrents -> torrents.mapNonZeroToMetric { it.seedersAvailable } }
  ),
  SEEDERS_CONNECTED(
    "The number of seeders this client is connected to.",
    MetricType.GAUGE,
    { torrents -> torrents.mapNonZeroToMetric { it.seedersConnected } }
  ),
  LEECHERS_AVAILABLE(
    "The number of leechers downloading this torrent (excluding oneself, if applicable). A " +
        "tracker's announce for the number of leechers takes precedence for this value; " +
        "otherwise qBt will take a best guess by polling the swarm.",
    MetricType.GAUGE,
    { torrents -> torrents.mapNonZeroToMetric { it.leechersAvailable } }
  ),
  LEECHERS_CONNECTED(
    "The number of leechers this client is connected to.",
    MetricType.GAUGE,
    { torrents -> torrents.mapNonZeroToMetric { it.leechersConnected } }
  ),
  RATIO(
    "The share ratio of this torrent. If a torrent is downloading, this ratio is calculated using " +
        "the total download bytes, but if the torrent is fully downloaded, the ratio is " +
        "calculated using the size of the torrent.",
    MetricType.GAUGE,
    { torrents -> torrents.mapAllToMetric { it.ratio } }
  ),
  ACTIVE_TIME_SECONDS(
    "The amount of time in seconds this torrent has spent \"started\" (that is, not complete " +
        "and not paused).",
    MetricType.COUNTER,
    { torrents -> torrents.mapAllToMetric { it.activeTimeSeconds } }
  ),
  UPLOAD_TOTAL_BYTES(
    "The number of uploaded bytes across all sessions including wasted data.",
    MetricType.COUNTER,
    { torrents -> torrents.mapAllToMetric { it.uploadTotalBytes } }
  ),
  UPLOAD_SESSION_BYTES(
    "The number of uploaded bytes including wasted data for the current session. A session is " +
        "defined by when the torrent is paused (including qBittorrent client shutdown).",
    MetricType.COUNTER,
    { torrents -> torrents.mapNonZeroToMetric { it.uploadSessionBytes } }
  ),
  UPLOAD_PAYLOAD_RATE_BYTES_PER_SECOND(
    "The upload rate of the torrent's payload data only (ie. doesn't include protocol chatter) " +
        "in bytes per second.",
    MetricType.COUNTER,
    { torrents -> torrents.mapNonZeroToMetric { it.uploadPayloadRateBytesPerSecond } }
  );

  companion object : MaindataCollectorContainer {
    private const val METRIC_NAME_PREFIX = "qbt_torrent_"

    private const val LABEL_TORRENT_NAME = "name"
    private const val LABEL_TORRENT_HASH = "hash"

    override fun collect(
      settings: MaindataCollectorContainerSettings,
      maindata: Maindata
    ): List<Metric> {
      val result = ArrayList<Metric>()
      settings.torrentsCollectors?.forEach { collector, collectorSettings ->
        if (collectorSettings.enabled == true) {
          result.add(collector.collect(maindata))
        }
      }
      return result
    }

    /** Creates a label map for the given [torrent] and [torrentHash]. */
    private fun createMetricLabel(torrent: Torrent, torrentHash: String): MetricLabel =
      MetricLabel.Builder()
        .addLabel(LABEL_TORRENT_NAME, torrent.displayName ?: "")
        .addLabel(LABEL_TORRENT_HASH, torrentHash)
        .build()

    /** Converts all (torrent/hash) pairs to (label/metric value) pairs. */
    private fun Map<String, Torrent>.mapAllToMetric(
      collector: (Torrent) -> Number?
    ): Map<MetricLabel, Number?> =
      mapKeys { entry -> createMetricLabel(entry.value, entry.key) }
        .mapValues { entry -> collector(entry.value) }

    /**
     * Converts all (torrent/hash) pairs to (label/metric value) pairs, filtering out those with
     * `null` or `0` metric value.
     */
    private fun Map<String, Torrent>.mapNonZeroToMetric(
      collector: (Torrent) -> Number?
    ): Map<MetricLabel, Number?> =
      filter { entry -> (collector(entry.value) ?: 0).toDouble() != 0.0 }
        .mapKeys { entry -> createMetricLabel(entry.value, entry.key) }
        .mapValues { entry -> collector(entry.value) }
  }

  private val metric: MultiMetric by lazy {
    MultiMetric(
      metrics = mapOf(),
      name = METRIC_NAME_PREFIX + name.toLowerCase(),
      help = help,
      type = metricType
    )
  }

  /** Returns the [Metric] for this collector using the passed [maindata]. */
  fun collect(maindata: Maindata): Metric {
    metric.metrics = update(maindata.torrents ?: mapOf())
    return metric
  }
}
