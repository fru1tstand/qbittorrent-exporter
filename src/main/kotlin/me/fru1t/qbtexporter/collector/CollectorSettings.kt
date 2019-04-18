package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings specific to collectors. */
data class CollectorSettings(
  @Documentation("Toggles for the qBittorrent maindata collectors.")
  var maindataCollectors: Map<String, Map<String, Boolean>>? =
    CollectorSettingsUtils.createDefaultSettings()
)
