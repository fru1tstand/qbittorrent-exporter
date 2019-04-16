package me.fru1t.qbtexporter.collector

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import org.junit.jupiter.api.Test

internal class CollectorSettingsHelperTest {
  private companion object {
    private val ALL_COLLECTORS: List<MaindataCollector> =
      listOf(*ServerStateCollector.values(), *TorrentsCollector.values())
  }

  @Test
  fun createDefaultSettings_containsAllCollectors() {
    val results = CollectorSettingsHelper.createDefaultSettings()

    // Flatten to a list of just string->boolean pairs
    val flattenedResults = HashMap<String, Boolean>()
    results.forEach { _, settings -> flattenedResults.putAll(settings) }

    // Remove collectors whose default setting is false (ie. what we expect)
    val testValues = ALL_COLLECTORS.toMutableList()
    testValues.removeIf { flattenedResults[CollectorSettingsHelper.getSettingsName(it)] == false }

    assertWithMessage(
      "Expected all collectors to have a default settings value, but found the following to be " +
          "enabled or missing:"
    )
      .that(testValues)
      .isEmpty()
  }

  @Test
  fun createDefaultSettings_allCollectorsDisabledByDefault() {
    val defaultSettings = CollectorSettingsHelper.createDefaultSettings()

    defaultSettings.forEach { _, settings ->
      settings.forEach { setting, value ->
        assertWithMessage("Expected default value of $setting to be false.")
          .that(value)
          .isFalse()
      }
    }
  }

  @Test
  fun getEnabledCollectors_returnsEmptyList_whenNoMapPresent() {
    assertThat(CollectorSettingsHelper.getEnabledCollectors(null))
      .isEmpty()
  }

  @Test
  fun getEnabledCollectors_returnAllCollectors_whenAllCollectorsAreEnabled() {
    val settings = CollectorSettingsHelper.createDefaultSettings()
    val enabledAllSettings = HashMap<String, Map<String, Boolean>>()
    settings.forEach { category, settings ->
      val enabledCategorySettings = HashMap<String, Boolean>()
      settings.forEach { setting, _ -> enabledCategorySettings[setting] = true }
      enabledAllSettings[category] = enabledCategorySettings
    }

    val result = CollectorSettingsHelper.getEnabledCollectors(enabledAllSettings)
    val testValues = ALL_COLLECTORS.toMutableList()
    testValues.removeIf { result.contains(it) }

    assertWithMessage(
      "Expected all collectors to be enabled, but found the following to be disabled:"
    )
      .that(testValues)
      .isEmpty()
  }

  @Test
  fun getEnabledCollectors_returnsArbitrarilyEnabledCollectors() {
    val settings = mapOf(
      Pair(
        "serverState",
        mapOf(
          Pair(
            CollectorSettingsHelper.getSettingsName(ServerStateCollector.ALL_TIME_UPLOAD_BYTES),
            true
          )
        )
      ),
      Pair(
        "torrents",
        mapOf(
          Pair(
            CollectorSettingsHelper.getSettingsName(TorrentsCollector.COMPLETED_BYTES),
            true
          )
        )
      )
    )
    val result = CollectorSettingsHelper.getEnabledCollectors(settings)

    assertThat(result)
      .containsAllOf(
        ServerStateCollector.ALL_TIME_UPLOAD_BYTES,
        TorrentsCollector.COMPLETED_BYTES
      )
  }
}
