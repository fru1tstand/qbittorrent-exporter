package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.settings.annotation.Documentation

/**
 * A settings object that only contains the option to enable or disable the collector. This class
 * is used instead of a [Boolean] within the settings map to future proof settings. The use of
 * this class forces Gson to serialize the setting as a map of the collector to an object instead of
 * a map of collector to boolean. This allows for future settings for the given collector which
 * can be appended to the object without full backwards compatibility.
 */
data class BasicCollectorSettings(
  @Documentation("Whether or not this collector is enabled.")
  var enabled: Boolean? = false
)
