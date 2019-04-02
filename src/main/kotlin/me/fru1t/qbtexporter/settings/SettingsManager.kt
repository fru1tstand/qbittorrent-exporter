package me.fru1t.qbtexporter.settings

import javax.inject.Provider

/** Methods to interface with the [Settings] for this exporter. */
interface SettingsManager : Provider<Settings> {
  /** Synchronously commits the settings to disk. */
  fun save()
}
