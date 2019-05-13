package me.fru1t.qbtexporter.collector.maindata

import me.fru1t.qbtexporter.collector.MaindataCollectorContainer
import me.fru1t.qbtexporter.collector.settings.MaindataCollectorContainerSettings
import me.fru1t.qbtexporter.collector.settings.maindata.AggregateTorrentCollectorSettings
import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.MultiMetric
import me.fru1t.qbtexporter.prometheus.metric.multimetric.MetricLabel
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.torrents.Torrent

/**
 * A set of collectors that transforms [Maindata.torrents] by aggregating a field into a metric.
 * This is usually accomplished by summing. Note most values that would traditionally be
 * [MetricType.COUNTER] are [MetricType.GAUGE] here because we only aggregate in-memory torrents
 * (ie. torrents that are currently loaded within qBittorrent), so if a torrent is deleted, its
 * metrics will no longer count in this collector.
 */
enum class AggregateTorrentCollector(
  val help: String,
  metricType: MetricType,
  private val aggregation: (Collection<Torrent>) -> Number?
) {
  DOWNLOAD_REMAINING_BYTES(
    "The total number of bytes remaining to download from all in-memory torrents, including " +
        "those of unwanted files.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.downloadRemainingBytes } }
  ),
  COMPLETED_BYTES(
    "The total number of 'actual' bytes completed from any source from all in-memory torrents. " +
        "'Actual' meaning blocks of data that have passed CRC and are verified to be " +
        "non-corrupt. 'Any source' as it's possible that a torrent's files can be pieced " +
        "together from outside qBittorrent.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.completedBytes } }
  ),
  DOWNLOAD_PAYLOAD_RATE_BYTES_PER_SECOND(
    "The summed download rate of all in-memory torrents' payload data.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.downloadPayloadRateBytesPerSecond?.toLong() } }
  ),
  DOWNLOAD_TOTAL_BYTES(
    "The number of downloaded bytes across all session from all in-memory torrents, including " +
        "wasted data.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.downloadTotalBytes } }
  ),
  DOWNLOAD_SESSION_BYTES(
    "The number of downloaded bytes from all in-memory torrents for this session, including " +
        "wasted data.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.downloadSessionBytes } }
  ),
  SEEDERS_AVAILABLE(
    "The total number of seeders from all in-memory torrents. Note this value can contain " +
        "duplicate counts if a peer shares multiple torrents.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.seedersAvailable?.toLong() } }
  ),
  LEECHERS_AVAILABLE(
    "The total number of leechers from all in-memory torrents. Note this value can contain " +
        "duplicate counts if a peer shares multiple torrents.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.leechersAvailable?.toLong() } }
  ),
  SEEDERS_CONNECTED(
    "The total number of seeders this client is connected to from all in-memory torrents. Note " +
        "this value can contain duplicate counts if a peer shares multiple torrents.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.seedersConnected?.toLong() } }
  ),
  LEECHERS_CONNECTED(
    "The total number of leechers this client is connected to from all in-memory torrents. Note " +
        "this value can contain duplicate counts if a peer shares multiple torrents.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.leechersConnected?.toLong() } }
  ),
  ACTIVE_TIME_SECONDS(
    "The total number of seconds all in-memory torrents have spent 'started' (that is, not " +
        "complete and not paused).",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.activeTimeSeconds?.toLong() } }
  ),
  SIZE_WANTED_BYTES(
    "The number of bytes wanted by all in-memory torrents. This value omits unwanted files.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.sizeWantedBytes } }
  ),
  SIZE_TOTAL_BYTES(
    "The number of bytes of all in-memory torrents regardless of 'want'.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.sizeTotalBytes } }
  ),
  UPLOAD_TOTAL_BYTES(
    "The number of uploaded bytes across all sessions from all in-memory torrents, including " +
        "wasted data.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.uploadTotalBytes } }
  ),
  UPLOAD_SESSION_BYTES(
    "The number of uploaded bytes for the current session, including wasted data, for all " +
        "in-memory torrents.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.uploadSessionBytes } }
  ),
  UPLOAD_PAYLOAD_RATE_BYTES_PER_SECOND(
    "The summed upload rate of all in-memory torrents' payload data.",
    MetricType.GAUGE,
    { torrents -> torrents.sum { it.uploadPayloadRateBytesPerSecond?.toLong() } }
  ),
  TORRENT_COUNT(
    "The number of torrents in this group.",
    MetricType.GAUGE,
    { torrents -> torrents.size }
  );

  companion object : MaindataCollectorContainer {
    private const val METRIC_NAME_PREFIX = "qbt_aggregate_torrent_"
    private val LABEL_SPECIAL_ALL = MetricLabel.Builder().addLabel("special", "all").build()
    private val LABEL_NAME_CATEGORY = "category"

    override fun collect(
      settings: MaindataCollectorContainerSettings,
      maindata: Maindata
    ): List<Metric> {
      val result = ArrayList<MultiMetric>()
      settings.aggregateTorrentCollectors?.forEach { collector, collectorSettings ->
        val metric = collector.collect(collectorSettings, maindata)
        if (!metric.isEmpty()) {
          result.add(metric)
        }
      }
      return result
    }

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

  private val metric: MultiMetric by lazy {
    MultiMetric(
      metrics = mapOf(),
      name = METRIC_NAME_PREFIX + name.toLowerCase(),
      help = help,
      type = metricType
    )
  }

  /**
   * Returns the [Metric] for this collector using the passed [maindata], respecting the given
   * [collectorSettings].
   */
  fun collect(
    collectorSettings: AggregateTorrentCollectorSettings,
    maindata: Maindata
  ): MultiMetric {
    val metrics = HashMap<MetricLabel, Number?>()

    if (collectorSettings.all == true) {
      metrics[LABEL_SPECIAL_ALL] = aggregation(maindata.torrents?.values ?: listOf())
    }

    if (collectorSettings.eachCategory == true) {
      val categories = HashSet<String?>()
      maindata.torrents?.values?.mapTo(categories) { it.category }

      categories.forEach { category ->
        val label = MetricLabel.Builder().addLabel(LABEL_NAME_CATEGORY, category ?: "").build()

        metrics[label] =
          aggregation(maindata.torrents?.values?.filter { it.category == category } ?: listOf())
      }
    }

    metric.metrics = metrics
    return metric
  }
}
