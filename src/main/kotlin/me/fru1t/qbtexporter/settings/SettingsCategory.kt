package me.fru1t.qbtexporter.settings

/** A natural grouping of metrics to group by in the settings file. */
enum class SettingsCategory {
  /**
   * Raw metrics exposed by the `server_state` object from the web ui. These metrics are similar to
   * that of the aggregate metrics; however, server-reported metrics persist data from deleted
   * torrents as well. Examples of server metrics include bandwidth caps, free space on disk, and
   * total bytes downloaded.
   */
  SERVER,

  /**
   * Metrics calculated by aggregating torrents. These metrics are similar to that of the server
   * metrics; however, aggregate metrics can only report non-deleted torrents from qBittorrent.
   * Examples of aggregate metrics include number of active torrents, current upload rate, and
   * current download rate.
   */
  AGGREGATE,

  /**
   * Raw metrics from individual torrents. For example, seed ratio for a specific torrent, torrent
   * category, and torrent upload amount.
   */
  PER_TORRENT
}
