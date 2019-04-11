package me.fru1t.qbtexporter.collector.maindata

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.MultiMetric
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.torrents.Torrent

enum class TorrentsCollector(
  help: String,
  metricType: MetricType,
  private val update: (Map<String, Torrent>) -> Map<Map<String, String>, Number?>
) {
  DATE_ADDED_UNIX_TIMESTAMP(
    "The unix timestamp (in seconds since unix epoch) this torrent was added.",
    MetricType.COUNTER,
    { torrents -> torrents.mapAllToMetric { torrent -> torrent.dateAddedUnixTimestamp ?: 0 } }
  ),

  UPLOAD_PAYLOAD_RATE_BYTES_PER_SECOND(
    "The upload rate of the torrent's payload data only (ie. doesn't include protocol chatter) " +
        "in bytes per second.",
    MetricType.COUNTER,
    { torrents ->
      torrents.mapNonZeroToMetric { torrent ->
        torrent.uploadPayloadRateBytesPerSecond ?: 0
      }
    }
  );

  private companion object {
    private const val METRIC_NAME_PREFIX = "qbt_torrent_"

    private const val LABEL_TORRENT_NAME = "name"
    private const val LABEL_TORRENT_HASH = "hash"

    /** Creates a label map for the given [torrent] and [torrentHash]. */
    private fun createTorrentLabelMap(torrent: Torrent, torrentHash: String): Map<String, String> {
      return mapOf(
        Pair(LABEL_TORRENT_HASH, torrentHash),
        Pair(LABEL_TORRENT_NAME, torrent.displayName ?: "")
      )
    }

    /** Converts all (torrent/hash) pairs to (label/metric value) pairs. */
    private fun Map<String, Torrent>.mapAllToMetric(
      collector: (Torrent) -> Number?
    ): Map<Map<String, String>, Number?> =
      mapKeys { entry -> createTorrentLabelMap(entry.value, entry.key) }
        .mapValues { entry -> collector(entry.value) }

    /**
     * Converts all (torrent/hash) pairs to (label/metric value) pairs, filtering out those with
     * `null` or `0` metric value.
     */
    private fun Map<String, Torrent>.mapNonZeroToMetric(
      collector: (Torrent) -> Number?
    ): Map<Map<String, String>, Number?> =
      filter { entry -> (collector(entry.value) ?: 0) != 0 }
        .mapKeys { entry -> createTorrentLabelMap(entry.value, entry.key) }
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

  /** Returns the [Metric] produced by this collector, given [maindata]. */
  fun collect(maindata: Maindata): Metric {
    metric.metrics = update(maindata.torrents ?: mapOf())
    return metric
  }
}
