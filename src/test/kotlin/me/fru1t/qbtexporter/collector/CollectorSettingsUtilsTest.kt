package me.fru1t.qbtexporter.collector

import com.google.common.truth.Truth.assertWithMessage
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import org.junit.jupiter.api.Test

internal class CollectorSettingsUtilsTest {
  private companion object {
    private val ALL_COLLECTORS: List<MaindataCollector> =
      listOf(*ServerStateCollector.values(), *TorrentsCollector.values())
  }

  @Test
  fun createDefaultSettings_containsAllCollectors() {
    val results = CollectorSettingsUtils.createDefaultSettings()

    // Flatten to a list of just string->boolean pairs
    val flattenedResults = HashMap<String, Boolean>()
    results.forEach { (_, settings) -> flattenedResults.putAll(settings) }

    // Remove collectors whose default setting is false (ie. what we expect)
    val testValues = ALL_COLLECTORS.toMutableList()
    testValues.removeIf { flattenedResults[CollectorSettingsUtils.getSettingsName(it)] == false }

    assertWithMessage(
      "Expected all collectors to have a default settings value, but found the following to be " +
          "enabled or missing:"
    )
      .that(testValues)
      .isEmpty()
  }

  @Test
  fun createDefaultSettings_allCollectorsDisabledByDefault() {
    val defaultSettings = CollectorSettingsUtils.createDefaultSettings()

    defaultSettings.forEach { (_, settings) ->
      settings.forEach { (setting, value) ->
        assertWithMessage("Expected default value of $setting to be false.")
          .that(value)
          .isFalse()
      }
    }
  }
}
