package me.fru1t.qbtexporter.exporter

import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings for the exporter web server. */
data class ExporterServerSettings(
  @Documentation("The port the exporter server should listen on to serve requests. [Default 9561]")
  var port: Int? = 9561,

  @Documentation(
    "The host the exporter server should listen on the serve requests. A host of '0.0.0.0' " +
        "listens on all addresses.")
  var host: String? = "0.0.0.0"
)
