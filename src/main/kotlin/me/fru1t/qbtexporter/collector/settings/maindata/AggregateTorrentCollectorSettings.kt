package me.fru1t.qbtexporter.collector.settings.maindata

import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings for individual collectors within the aggregate torrent collector container. */
data class AggregateTorrentCollectorSettings(
  @Documentation("Special category that aggregates this metric from all torrents loaded in qBt.")
  var all: Boolean? = false
)
