package me.fru1t.qbtexporter.collector

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import org.junit.jupiter.api.Test

internal class MaindataCollectorContainerSettingsTest {
  @Test
  fun defaultSettings_containsAllTorrentsCollectors() {
    val settings = MaindataCollectorContainerSettings()

    assertThat(settings.torrentsCollectors!!.size).isEqualTo(TorrentsCollector.values().size)
  }
}
