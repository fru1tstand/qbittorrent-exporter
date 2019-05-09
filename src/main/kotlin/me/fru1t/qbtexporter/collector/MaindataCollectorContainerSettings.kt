package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.settings.annotation.Documentation

/** Settings for the maindata collector containers. */
data class MaindataCollectorContainerSettings(
  @Documentation("A placeholder setting that doesn't do anything.")
  var placeholder: String? = "placeholder"
)
