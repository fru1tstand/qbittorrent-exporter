package me.fru1t.qbtexporter.qbt

import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings specific to qBittorrent. */
data class QbtSettings(
  @Documentation("The address qBittorrent is listening on to serve its web ui. Please include " +
      "protocol, host, and port. Do not include a trailing slash.")
  var webUiAddress: String? = "http://localhost:8080"
)
