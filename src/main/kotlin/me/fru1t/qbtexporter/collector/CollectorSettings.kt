package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings specific to collectors. */
data class CollectorSettings(
  @Documentation(
    "Toggles for the qBittorrent maindata collectors. Warning: even though a collector may " +
        "be enabled in settings, it may not show up in the `/metrics` page if all its slices' " +
        "values are zero.")
  var maindataCollectors: Map<String, Map<String, Boolean>>? =
    CollectorSettingsUtils.createDefaultSettings()
)
