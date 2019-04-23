package me.fru1t.qbtexporter.settings

import me.fru1t.qbtexporter.kotlin.LazyRelay

/** Methods to interface with the [Settings] for this exporter. */
interface SettingsManager {
  /** Returns the relay that provides the actual [Settings] object. */
  fun getSettingsRelay(): LazyRelay<Settings>
}
