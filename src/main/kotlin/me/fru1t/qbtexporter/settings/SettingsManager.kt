package me.fru1t.qbtexporter.settings

import me.fru1t.common.kotlin.Relayer
import javax.inject.Provider

/** Methods to interface with the [Settings] for this exporter. */
interface SettingsManager : Provider<Settings>, Relayer<Settings> {
  /** Synchronously commits the settings to disk. */
  fun save()

  /** Returns the current settings loaded in memory. */
  override fun get(): Settings

  /**
   * Returns the signal used to determine whether or not the settings should be reloaded from disk.
   */
  override fun signal(): Long

  /**
   * Returns a freshly computed [Settings] object from the file on disk. Note this does not update
   * the value returned by [get].
   */
  override fun calculate(): Settings
}
