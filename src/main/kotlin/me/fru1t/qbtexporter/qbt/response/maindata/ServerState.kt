package me.fru1t.qbtexporter.qbt.response.maindata

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Since
import me.fru1t.qbtexporter.qbt.QbtVersion

/**
 * Represents metrics for the state of the server as a whole obtained from the `server_state` field
 * of the root `maindata` json object since qBt 4.1.5. Descriptions and types are decoded from the
 * qBittorrent source file
 * [synccontroller.cpp](https://github.com/qbittorrent/qBittorrent/blob/v4_1_x/src/webui/api/synccontroller.cpp).
 */
data class ServerState(
  // All time metrics
  /** Total downloaded bytes from all torrents (including deleted ones) across all sessions. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("alltime_dl")
  val allTimeDownloadedBytes: Long?,

  /** Total uploaded bytes from all torrents (including deleted ones) across all sessions. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("alltime_ul")
  val allTimeUploadedBytes: Long?,

  /**
   * Share ratio calculated by taking all time upload divided by all time download and rounding to
   * two decimal places.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("global_ratio")
  val allTimeShareRatio: String?,

  // Status
  /** The network status which is one of: `connected`, `disconnected`, or `firewalled`. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("connection_status")
  val connectionStatus: String?,

  /** The frequency at which the torrents table should refresh (used in the webUI) in ms. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("refresh_interval")
  val refreshIntervalMs: Int?,

  /** Whether or not the qBt torrent queueing system is enabled. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("queueing")
  val isTorrentQueueingEnabled: Boolean?,

  // I/O - Most of the source of these data are found in
  // https://github.com/qbittorrent/qBittorrent/blob/v4_1_x/src/base/bittorrent/cachestatus.h
  /**
   * The average time an I/O request stays in the queue. It's unknown whether this is solely reads,
   * writes, or both.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("average_time_queue")
  val diskAverageCacheQueueTimeMs: Long?,

  /**
   * The number of used bytes for the disk cache. This number is independent from the maximum size
   * of the disk cache. It's unknown whether this value represents the read cache, write cache, or
   * both.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("total_buffers_size")
  val diskCacheSizeBytes: Long?,

  /** The number of bytes available in the default save path. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("free_space_on_disk")
  val diskFreeSpaceBytes: Long?,

  /**
   * The number of disk jobs queued up in the disk cache. It's unknown whether this is solely reads,
   * writes, or both.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("queued_io_jobs")
  val diskQueueJobCount: Long?,

  /**
   * The number of bytes pending to be flushed to/from disk to/from the disk cache. It's unknown
   * whether this value represents the read cache, write cache, or both.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("total_queued_size")
  val diskQueueSizeBytes: Long?,

  /**
   * The percent (rounded to two decimal places) of disk read requests that were handled by the
   * read cache.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("read_cache_hits")
  val diskReadCacheHitPercent: String?,

  /**
   * Percent of connected peers that are issuing read requests (from disk or cache). Literally
   * `reading_peers / total_peers`. qBt obtains the number of "reading peers" through `libtorrent`
   * which doesn't give much more insight as to what this value means. See `peer.num_peers_up_disk`
   * in the [libtorrent docs](https://www.libtorrent.org/manual-ref.html).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("read_cache_overload")
  val diskReadCacheOverloadPercent: String?,

  /**
   * Percent of connected peers that are issuing write requests (to disk or cache). Literally
   * `writing_peers / total_peers`. qBt obtains the number of "writing peers" through `libtorrent`
   * which doesn't give much more insight as to what this value means. See
   * `peer.num_peers_down_disk` in the
   * [libtorrent docs](https://www.libtorrent.org/manual-ref.html).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("write_cache_overload")
  val diskWriteCacheOverloadPercent: String?,

  // Network - Most of the source of these data are found in
  // https://github.com/qbittorrent/qBittorrent/blob/v4_1_x/src/base/bittorrent/sessionstatus.h
  /** The number of DHT nodes connected. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("dht_nodes")
  val networkDhtNodeCount: Long?,

  /** The number of bytes downloaded this session. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("dl_info_data")
  val currentSessionDownloadedBytes: Long?,

  /** The number of bytes uploaded this session. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("up_info_data")
  val currentSessionUploadedBytes: Long?,

  /**
   * The number of bytes wasted this session. Waste is a product of corrupt or incomplete torrent
   * blocks which are discarded.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("total_wasted_session")
  val currentSessionBytesWasted: Long?,

  /** Whether or not qBt is using alternative speed limits set by the user. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("use_alt_speed_limits")
  val networkIsAlternativeSpeedLimitsEnabled: Boolean?,

  /** The number of peer connections. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("total_peer_connections")
  val networkPeerConnectionCount: Long?,

  /** The instantaneous download rate in bytes per second. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("dl_info_speed")
  val networkDownloadSpeedBytesPerSecond: Long?,

  /** The user set maximum download rate allowed in bytes per second. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("dl_rate_limit")
  val networkDownloadSpeedCapBytesPerSecond: Long?,

  /** The instantaneous upload rate in bytes per second. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("up_info_speed")
  val networkUploadSpeedBytesPerSecond: Long?,

  /** The user set maximum upload rate allowed in bytes per second. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("up_rate_limit")
  val networkUploadSpeedCapBytesPerSecond: Long?
)
