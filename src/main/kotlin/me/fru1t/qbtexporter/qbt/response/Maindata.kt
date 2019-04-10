package me.fru1t.qbtexporter.qbt.response

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Since
import me.fru1t.qbtexporter.qbt.QbtVersion
import me.fru1t.qbtexporter.qbt.response.maindata.ServerState
import me.fru1t.qbtexporter.qbt.response.maindata.categories.Category
import me.fru1t.qbtexporter.qbt.response.maindata.torrents.Torrent

/**
 * The root json object returned from qBittorrent's `api/v2/sync/maindata` api call. Values are
 * verified from qBittorrent source code. See
 * [synccontroller.cpp](https://github.com/qbittorrent/qBittorrent/blob/master/src/webui/api/synccontroller.cpp).
 *
 * This class currently does not implement `torrents_removed` or `categories_removed` as we'll
 * almost always be obtaining a full update. If that condition changes, though, there's nothing else
 * preventing the `*_removed` fields from being implemented in this class.
 */
data class Maindata(
  /**
   * Whether or not the qBt response is a complete update (ie. not just the difference since last
   * update). Diffs are done against [responseId]. This value should always be `true` if no
   * [responseId] is passed in as a `GET` parameter when querying the `maindata` api.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("full_update")
  val isFullUpdate: Boolean? = null,

  /**
   * The server-generated response ID which can be used in the next `maindata` API request to
   * obtain a diff update since the current call (as opposed to a full update).
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("rid")
  val responseId: Int? = null,

  /** See [ServerState]. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("server_state")
  val serverState: ServerState? = null,

  /** A map of category names to [Category] data. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("categories")
  val categories: Map<String, Category>? = null,

  /**
   * A map of torrent hashes to [Torrent] data. Note the [Torrent] object does not contain its own
   * hash.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("torrents")
  val torrents: Map<String, Torrent>? = null
)
