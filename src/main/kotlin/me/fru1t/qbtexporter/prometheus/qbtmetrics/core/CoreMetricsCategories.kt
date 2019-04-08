package me.fru1t.qbtexporter.prometheus.qbtmetrics.core

/** The groups for core metrics. */
enum class CoreMetricsCategories {
  /**
   * Raw metrics exposed by the `server_state` object from the web ui. These metrics are similar to
   * that of the aggregate metrics; however, server-reported metrics persist data from deleted
   * torrents as well. Examples of server metrics include bandwidth caps, free space on disk, and
   * total bytes downloaded.
   */
  SERVER,

  /**
   * Raw metrics from individual torrents. For example, seed ratio for a specific torrent, torrent
   * category, and torrent upload amount.
   */
  PER_TORRENT
}
