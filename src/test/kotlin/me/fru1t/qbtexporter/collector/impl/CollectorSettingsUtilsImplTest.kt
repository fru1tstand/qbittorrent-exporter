package me.fru1t.qbtexporter.collector.impl

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.nhaarman.mockitokotlin2.whenever
import me.fru1t.qbtexporter.collector.CollectorSettings
import me.fru1t.qbtexporter.collector.CollectorSettingsUtils
import me.fru1t.qbtexporter.collector.CollectorSettingsUtils.Companion.getSettingsName
import me.fru1t.qbtexporter.collector.MaindataCollector
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import me.fru1t.qbtexporter.kotlin.testing.TestLazyRelay
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.SettingsManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class CollectorSettingsUtilsImplTest {
  private companion object {
    private val ALL_MAINDATA_COLLECTORS: List<MaindataCollector> =
      listOf(*ServerStateCollector.values(), *TorrentsCollector.values())
  }

  @Mock private lateinit var mockSettingsManager: SettingsManager

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    whenever(mockSettingsManager.getSettingsRelay()).thenReturn(TestLazyRelay.of(Settings()))
  }

  @Test
  fun getEnabledMaindataCollectors_returnsEmptyList_whenNoMapPresent() {
    whenever(mockSettingsManager.getSettingsRelay()).thenReturn(TestLazyRelay.of(Settings()))

    assertThat(createUtils().getEnabledMaindataCollectors())
      .isEmpty()
  }

  @Test
  fun getEnabledMaindataCollectors_returnAllMaindataCollectors_whenAllAreEnabled() {
    val settings = CollectorSettingsUtils.createDefaultSettings()
    val maindataCollectorSettings = HashMap<String, Map<String, Boolean>>()
    settings.forEach { (category, settings) ->
      val enabledCategorySettings = HashMap<String, Boolean>()
      settings.forEach { (setting, _) -> enabledCategorySettings[setting] = true }
      maindataCollectorSettings[category] = enabledCategorySettings
    }
    whenever(mockSettingsManager.getSettingsRelay())
      .thenReturn(
        TestLazyRelay.of(
          Settings(
            collectorSettings = CollectorSettings(
              maindataCollectors = maindataCollectorSettings
            )
          )
        )
      )

    val result = createUtils().getEnabledMaindataCollectors()
    val testValues = ALL_MAINDATA_COLLECTORS.toMutableList()
    testValues.removeIf { result.contains(it) }

    assertWithMessage(
      "Expected all collectors to be enabled, but found the following to be disabled:"
    )
      .that(testValues)
      .isEmpty()
  }

  @Test
  fun getEnabledMaindataCollectors_returnsArbitrarilyEnabledCollectors() {
    val maindataCollectors = mapOf(
      Pair(
        "serverState",
        mapOf(Pair(getSettingsName(ServerStateCollector.ALL_TIME_UPLOAD_BYTES), true))
      ),
      Pair(
        "torrents",
        mapOf(Pair(getSettingsName(TorrentsCollector.COMPLETED_BYTES), true))
      )
    )
    whenever(mockSettingsManager.getSettingsRelay())
      .thenReturn(
        TestLazyRelay.of(
          Settings(
            collectorSettings = CollectorSettings(
              maindataCollectors = maindataCollectors
            )
          )
        )
      )

    val result = createUtils().getEnabledMaindataCollectors()

    assertThat(result)
      .containsAllOf(
        ServerStateCollector.ALL_TIME_UPLOAD_BYTES,
        TorrentsCollector.COMPLETED_BYTES
      )
  }

  private fun createUtils(): CollectorSettingsUtilsImpl =
    CollectorSettingsUtilsImpl(settingsManager = mockSettingsManager)
}
