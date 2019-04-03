package me.fru1t.qbtexporter.settings

import me.fru1t.qbtexporter.qbt.QbtSettings
import me.fru1t.qbtexporter.settings.annotation.Documentation

/**
 * Settings for the qBittorrentExporter. Fetch the instance of Settings through [SettingsManager].
 *
 * Settings must follow the following rules:
 * + No individual settings should exist in the root [Settings] object. Instead, create a
 *   module-specific settings data class and add the individual setting there. Then add the module's
 *   settings data class to this root [Settings] class.
 * + Each setting *must* have a [Documentation] annotation that serves as user documentation for how
 *   to use the setting (even the module settings class that's defined here).
 * + Settings data classes must be pure data classes with no other logic or fields outside those
 *   defined in the primary constructor.
 * + Settings that are deprecated should be annotation with [Deprecated] with at least
 *   [Deprecated.message] filled out, and [Deprecated.replaceWith] if there is a viable replacement.
 * + Settings must be a `null`-able, optional, `var`.
 * + Setting types must either be primitives, collections, or another data class (ie. nested
 *   settings).
 */
data class Settings(
  @Documentation("Settings specific to qBittorrent.")
  var qbtSettings: QbtSettings? = QbtSettings()
)
