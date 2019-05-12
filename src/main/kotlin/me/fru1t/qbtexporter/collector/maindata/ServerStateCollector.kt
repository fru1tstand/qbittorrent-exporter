package me.fru1t.qbtexporter.collector.maindata

import me.fru1t.qbtexporter.collector.MaindataCollectorContainer
import me.fru1t.qbtexporter.collector.settings.MaindataCollectorContainerSettings
import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.SingleMetric
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.ServerState

/** A set of collectors that transform [Maindata.serverState] fields into metrics. */
enum class ServerStateCollector(
  val help: String,
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
  ),
  ALL_TIME_SHARE_RATIO(
    "The share ratio calculated by taking the all time upload bytes divided by the all time " +
        "download bytes and rounding to two decimal places.",
    MetricType.GAUGE,
    { serverState -> serverState?.allTimeShareRatio?.toDouble() }
  ),
  DISK_AVERAGE_CACHE_QUEUE_TIME_MS(
    "The average time an I/O disk request stays in the queue in milliseconds.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskAverageCacheQueueTimeMs }
  ),
  DISK_CACHE_SIZE_BYTES(
    "The number of used bytes (not the maximum) for the disk cache.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskCacheSizeBytes }
  ),
  DISK_FREE_SPACE_BYTES(
    "The number of bytes available in the default qBittorrent save path.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskFreeSpaceBytes }
  ),
  DISK_QUEUE_JOB_COUNT(
    "The number of disk jobs queued up on the disk cache.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskQueueJobCount }
  ),
  DISK_QUEUE_SIZE_BYTES(
    "The number of bytes pending to be flushed to/from disk to/from the disk cache.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskQueueSizeBytes }
  ),
  DISK_READ_CACHE_HIT_PERCENT(
    "The percent (rounded to two decimal places) of disk read requests that were handled by the " +
        "read cache.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskReadCacheHitPercent?.toDouble() }
  ),
  DISK_READ_CACHE_OVERLOAD_PERCENT(
    "The percent of connected peers that are issuing read requests (from disk or cache). " +
        "Literally, the number of peers utilizing disk read I/O divided by the total number " +
        "of peers connected.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskReadCacheOverloadPercent?.toDouble() }
  ),
  DISK_WRITE_CACHE_OVERLOAD_PERCENT(
    "The percent of connected peers that are issuing write requests (from disk or cache). " +
        "Literally, the number of peers utilizing disk write I/O divided by the total number " +
        "of peers connected.",
    MetricType.GAUGE,
    { serverState -> serverState?.diskWriteCacheOverloadPercent?.toDouble() }
  ),
  NETWORK_DHT_NODE_COUNT(
    "The number of DHT nodes connected.",
    MetricType.GAUGE,
    { serverState -> serverState?.networkDhtNodeCount }
  ),
  CURRENT_SESSION_DOWNLOADED_BYTES(
    "The number of bytes downloaded this session.",
    MetricType.COUNTER,
    { serverState -> serverState?.currentSessionDownloadedBytes }
  ),
  CURRENT_SESSION_UPLOADED_BYTES(
    "The number of bytes uploaded this session.",
    MetricType.COUNTER,
    { serverState -> serverState?.currentSessionUploadedBytes }
  ),
  CURRENT_SESSION_WASTED_BYTES(
    "The number of bytes wasted this session.",
    MetricType.COUNTER,
    { serverState -> serverState?.currentSessionBytesWasted }
  ),
  NETWORK_PEER_CONNECTION_COUNT(
    "The number of peers connected to.",
    MetricType.GAUGE,
    { serverState -> serverState?.networkPeerConnectionCount }
  ),
  NETWORK_DOWNLOAD_SPEED_BYTES_PER_SECOND(
    "The instantaneous download rate in bytes per second.",
    MetricType.GAUGE,
    { serverState -> serverState?.networkDownloadSpeedBytesPerSecond }
  ),
  NETWORK_DOWNLOAD_SPEED_CAP_BYTES_SER_SECOND(
    "The user set maximum download rate in bytes per second.",
    MetricType.GAUGE,
    { serverState -> serverState?.networkDownloadSpeedCapBytesPerSecond }
  ),
  NETWORK_UPLOAD_SPEED_BYTES_PER_SECOND(
    "The instantaneous upload rate in bytes per second.",
    MetricType.GAUGE,
    { serverState -> serverState?.networkUploadSpeedBytesPerSecond }
  ),
  NETWORK_UPLOAD_SPEED_CAP_BYTES_PER_SECOND(
    "The user set maximum upload rate allowed in bytes per second.",
    MetricType.GAUGE,
    { serverState -> serverState?.networkUploadSpeedCapBytesPerSecond }
  );

  companion object : MaindataCollectorContainer {
    private const val METRIC_NAME_PREFIX = "qbt_server_state_"

    override fun collect(
      settings: MaindataCollectorContainerSettings,
      maindata: Maindata
    ): List<Metric> {
      val result = ArrayList<Metric>()
      settings.serverStateCollectors?.forEach { collector, collectorSettings ->
        if (collectorSettings.enabled == true) {
          result.add(collector.collect(maindata))
        }
      }
      return result
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

  /** Returns the [Metric] for this collector using the passed [maindata]. */
  fun collect(maindata: Maindata): Metric {
    metric.value = update(maindata.serverState)
    return metric
  }
}
