package me.fru1t.qbtexporter.settings

import me.fru1t.common.kotlin.LazyRelayer
import javax.inject.Provider

/** Methods to interface with the [Settings] for this exporter. */
abstract class SettingsManager : LazyRelayer<Settings>(), Provider<Settings> {
  /** Synchronously commits the settings to disk. */
  abstract fun save()

  /** Returns the last time the settings were modified. Useful for using a LazyRelay. */
  abstract fun getLastUpdatedTimeMs(): Long

  /** Returns the current settings loaded in memory. */
  @Deprecated("Use property delegation method #relay instead.")
  abstract override fun get(): Settings

  /** A signal for when to update settings. */
  abstract override fun signal(): Any?

  /** How settings are loaded via the implementation. */
  abstract override fun calculate(): Settings
}
