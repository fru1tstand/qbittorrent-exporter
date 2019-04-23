package me.fru1t.qbtexporter.collector.impl

import me.fru1t.qbtexporter.collector.CollectorSettingsUtils
import me.fru1t.qbtexporter.collector.MaindataCollector
import me.fru1t.qbtexporter.kotlin.LazyRelay
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.SettingsManager
import javax.inject.Inject

/** Implementation of [CollectorSettingsUtils]. */
class CollectorSettingsUtilsImpl @Inject constructor(
  settingsManager: SettingsManager
) : CollectorSettingsUtils {
  private val settingsRelay: LazyRelay<Settings> = settingsManager.getSettingsRelay()

  override fun getEnabledMaindataCollectors(): List<MaindataCollector> {
    val result = ArrayList<MaindataCollector>()
    CollectorSettingsUtils.maindataCollectorSettingsMap.forEach { (settingsCategory, collectors) ->
      val categorySettings =
        settingsRelay.poll().collectorSettings?.maindataCollectors?.get(settingsCategory) ?: mapOf()
      collectors.forEach { collector ->
        if (categorySettings[CollectorSettingsUtils.getSettingsName(collector)] == true) {
          result.add(collector)
        }
      }
    }
    return result
  }
}
