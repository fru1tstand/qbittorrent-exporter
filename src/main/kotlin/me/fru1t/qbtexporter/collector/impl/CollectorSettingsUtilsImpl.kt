package me.fru1t.qbtexporter.collector.impl

import me.fru1t.common.kotlin.lazyRelay
import me.fru1t.qbtexporter.collector.CollectorSettingsUtils
import me.fru1t.qbtexporter.collector.MaindataCollector
import me.fru1t.qbtexporter.logger.Logger
import me.fru1t.qbtexporter.settings.SettingsManager
import javax.inject.Inject

/** Implementation of [CollectorSettingsUtils]. */
class CollectorSettingsUtilsImpl @Inject constructor(
  settingsManager: SettingsManager,
  logger: Logger
) : CollectorSettingsUtils {
  private val settings: Map<String, Map<String, Boolean>>?
      by lazyRelay(settingsManager::getLastUpdatedTimeMs) {
        logger.i("Updated maindata collectors")
        settingsManager.get().collectorSettings?.maindataCollectors
      }

  override fun getEnabledMaindataCollectors(): List<MaindataCollector> {
    val result = ArrayList<MaindataCollector>()
    CollectorSettingsUtils.maindataCollectorSettingsMap.forEach { (settingsCategory, collectors) ->
      val categorySettings = settings?.get(settingsCategory) ?: mapOf()
      collectors.forEach { collector ->
        if (categorySettings[CollectorSettingsUtils.getSettingsName(collector)] == true) {
          result.add(collector)
        }
      }
    }
    return result
  }
}
