package me.fru1t.qbtexporter.qbt.data.maindata.torrents

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Since
import me.fru1t.qbtexporter.qbt.QbtVersion

/**
 * Represents metrics for a single torrent, obtained through the `torrents` map from the `maindata`
 * api request. Descriptions and types are obtained through the qBittorrent source file
 * [torrenthandle.cpp](https://github.com/qbittorrent/qBittorrent/blob/v4_1_x/src/base/bittorrent/torrenthandle.cpp)
 * and
 * [serialize_torrent.cpp](https://github.com/qbittorrent/qBittorrent/blob/v4_1_x/src/webui/api/serialize/serialize_torrent.cpp).
 *
 * Many of these values are internally backed by libtorrent's
 * [torrent_handle](https://www.libtorrent.org/reference-Core.html#torrent_handle),
 * [torrent_status](https://www.libtorrent.org/reference-Core.html#torrent_status), and
 * [torrent_info](https://www.libtorrent.org/reference-Core.html#torrent_info) classes.
 */
data class Torrent(
  /**
   * The unix timestamp (seconds since unix epoch) this torrent was added.
   *
   * Warning: this should be a [UInt]. See
   * [QDateTime::toTime_t()](https://doc.qt.io/archives/qt-4.8/qdatetime.html#toTime_t).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("added_on")
  val dateAddedUnixTimestamp: Int?,

  /**
   * The unix timestamp (seconds since unix epoch) this torrent was completed (that is, all pieces
   * are in possession).
   *
   * Warning: this should be a [UInt]. See
   * [QDateTime::toTime_t()](https://doc.qt.io/archives/qt-4.8/qdatetime.html#toTime_t).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("completion_on")
  val dateCompletedUnixTimestamp: Int?,

  /** The amount of bytes remaining, to download. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("amount_left")
  val downloadRemainingBytes: Long?,

  /**
   * Whether or not the torrent has been set to be automatically managed. Automatic torrent
   * management (called TMM) is when qBittorrent employs a queueing system for torrent downloading,
   * limiting the number of active torrents.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("auto_tmm")
  val isAutomaticallyManaged: Boolean?,

  /** The torrent's category, if any. In qBittorent, a torrent can only have a single category. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("category")
  val category: String?,

  /**
   * The number of "actual" bytes completed from any source. "Actual" meaning, data that's passed
   * CRC and is verified to be non-corrupt. Any source as it's possible that a torrent could be
   * pieced together from sources other than that of the torrent swarm.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("completed")
  val completedBytes: Long?,

  /**
   * The set maximum download rate in bytes per second. See
   * `libtorrent.torrent_status.download_limit`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("dl_limit")
  val downloadRateLimitBytesPerSecond: Int?,

  /**
   * The download rate of the torrent's payload only (ie. doesn't include protocol chatter) in
   * bytes per second. See `libtorrent.torrent_status.download_payload_rate`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("dlspeed")
  val downloadPayloadRateBytesPerSecond: Int?,

  /**
   * The number of downloaded bytes across all sessions including wasted data. See
   * `libtorrent.torrent_status.all_time_download`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("downloaded")
  val downloadTotalBytes: Long?,

  /**
   * The number of downloaded bytes, including wasted data, for the current session (a session is
   * reset when a torrent is paused which includes qBittorrent client shutdown). See
   * `libtorrent.torrent_status.total_download`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("downloaded_session")
  val downloadSessionBytes: Long?,

  /**
   * The number of seconds qBittorrent predicts the torrent will remain active. For downloads, this
   * means the time until completion. For uploads, this means the time until seeding is turned off.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("eta")
  val etaSeconds: Long?,

  /**
   * Whether or not the first and last pieces have high priority, with all pieces in between having
   * normal or higher priority.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("f_l_piece_prio")
  val hasFirstAndLastPiecePriority: Boolean?,

  /**
   * Whether or not this torrent was force started. Force start is a qBittorrent concept that means
   * the torrent is not under automatic torrent management mode (ie. it has bypassed torrent
   * queueing).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("force_start")
  val isForceStarted: Boolean?,

  /**
   * The unix timestamp (seconds since unix epoch) since the last time this torrent was downloading
   * or uploading anything to/from another peer. See `libtorrent.last_upload` or `last_download`.
   *
   * Warning: this should be a [UInt]. See
   * [QDateTime::toTime_t()](https://doc.qt.io/archives/qt-4.8/qdatetime.html#toTime_t).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("last_activity")
  val dateSinceLastActivityUnixTimestamp: Int?,

  /** The magnet uri for this torrent. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("magnet_uri")
  val magnetUri: String?,

  /** The user set maximum share ratio this torrent should stop seeding at or `-1` for infinite. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("max_ratio")
  val maxRatio: Double?,

  /** The user set amount of minutes this torrent should seed for. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("max_seeding_time")
  val maxSeedTimeMinutes: Int?,

  /** The display name for this torrent. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("name")
  val displayName: String?,

  /**
   * The number of seeders seeding this torrent. A tracker's announce for the number of seeders
   * takes precedence; otherwise, this metric will report the number of seeders the client has
   * detected in the swarm.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("num_complete")
  val seedersAvailable: Int?,

  /**
   * The number of leechers downloading this torrent. A tracker's announce for the number of
   * leechers take precedence; otherwise, this metric will report the number of leechers the client
   * has detected in the swarm.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("num_incomplete")
  val leechersAvailable: Int?,

  /** The number of leechers this client is connected to. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("num_leechs")
  val leechersConnected: Int?,

  /** The number of seeders this client is connected to. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("num_seeds")
  val seedersConnected: Int?,

  /**
   * The position this torrent is in within qBittorrent's automatic torrent management (TMM)
   * queueing system with `1` being the highest" priority. This value is only set while the torrent
   * is queued or downloading.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("priority")
  val queuePosition: Int?,

  /**
   * A decimal from `0.00` to `1.00` denoting how much of the torrent has been downloaded. This
   * value is rounded to the nearest two decimal places.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("progress")
  val progressDecimal: Double?,

  /**
   * The share ratio of this torrent. This is calculated by dividing the all time upload bytes
   * by the all time download bytes. However, if the all time download bytes is less than 1% of the
   * torrent, the share ratio is calculated by dividing the all time upload bytes by the total
   * size of the torrent (instead of the all time download).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("ratio")
  val ratio: Double?,

  /**
   * Same as [maxRatio] except this value is stored internally by qBittorrent in case the torrent
   * needs to be restored. `-1` means infinite. `-2` means the torrent will use the global share
   * ratio.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("ratio_limit")
  val internallyStoredMaxRatio: Double?,

  /** The absolute save path for the torrent. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("save_path")
  val savePath: String?,

  /**
   * The same as [maxSeedTimeMinutes] except this value is stored internally by qBittorrent in case
   * the torrent needs to be restored. `-1` means infinite. `-2` means to torrent will use the
   * global max seeding time.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("seeding_time_limit")
  val internallyStoredMaxSeedTimeMinutes: Int?,

  /**
   * The last unix timestamp (seconds since unix epoch) this torrent collectively had a 100% file
   * availability as a result of all peers.
   *
   * Note this should really be a [UInt].
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("seen_complete")
  val lastSeenCompleteUnixTimestamp: Int?,

  /**
   * Whether or not "sequential download" is enabled. Sequential download is a mode in libtorrent
   * where pieces of a torrent are downloaded in sequential order as opposed to rarest-first. This
   * state seems to be deprecated with no replacement. See
   * [sequential_download](https://www.libtorrent.org/include/libtorrent/torrent_status.hpp).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("seq_dl")
  val isSequentiallyDownloading: Boolean?,

  /** The size of this torrent minus the unwanted files, in bytes. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("size")
  val sizeWantedBytes: Long?,

  /**
   * A short descriptive string status of the current torrent.
   *
   * Possible values are:
   * - `error`
   * - `missingFiles`
   * - `uploading`
   * - `pausedUP` (paused while seeding/uploading)
   * - `queuedUP` (queued to seed/upload)
   * - `stalledUP` (stalled while seeding/uploading)
   * - `checkingUP` (checking file integrity while seeding/uploading)
   * - `forcedUP` (forced resumed to seed/upload)
   * - `allocating`
   * - `downloading`
   * - `metaDL` (downloading metadata)
   * - `pausedDL`
   * - `queuedDL`
   * - `stalledDL`
   * - `checkingDL`
   * - `forcedDL`
   *
   * See
   * [serialize_torrent#line41](https://github.com/qbittorrent/qBittorrent/blob/v4_1_x/src/webui/api/serialize/serialize_torrent.cpp#L41)
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("state")
  val status: String?,

  /** Whether or not super seeding is enabled for this torrent. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("super_seeding")
  val isSuperSeeding: Boolean?,

  /**
   * A comma-space delimited list of tags this torrent is attached to. For example
   * `tag1, tag2, tag3`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("tags")
  val tags: String?,

  /**
   * The amount of time in seconds this torrent has spent "started" (that is, not complete and not
   * paused). See `libtorrent.torrent_status.active_time`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("time_active")
  val activeTimeSeconds: Int?,

  /** The total number of bytes the this torrent contains regardless of "want". */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("total_size")
  val sizeTotalBytes: Long?,

  /**
   * The URL of the last working tracker. If no tracker request has been successful yet, this will
   * be an empty string. See `libtorrent.torrent_info.current_tracker`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("tracker")
  val currentTracker: String?,

  /**
   * The user-imposed maximum upload rate in bytes per second this torrent should upload at. Value
   * is `-1` for no limit. See `libtorrent.torrent_handle.upload_limit`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("up_limit")
  val uploadRateLimitBytesPerSecond: Int?,

  /**
   * The number of uploaded bytes across all sessions including wasted data. See
   * `libtorrent.torrent_status.all_time_upload`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("uploaded")
  val uploadTotalBytes: Long?,

  /**
   * The number of uploaded bytes including wasted data, for the current session (a session is reset
   * when a torrent is paused which includes qBittorrent client shutdown). See
   * `libtorrent.torrent_status.total_upload`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("uploaded_session")
  val uploadSessionBytes: Long?,

  /**
   * The upload rate of the torrent's payload data only (ie. doesn't include protocol chatter) in
   * bytes per second. See `libtorrent.torrent_status.upload_payload_rate`.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("upspeed")
  val uploadPayloadRateBytesPerSecond: Int?
)
