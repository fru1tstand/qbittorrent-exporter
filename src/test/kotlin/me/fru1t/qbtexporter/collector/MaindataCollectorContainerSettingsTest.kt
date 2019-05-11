package me.fru1t.qbtexporter.collector

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.collector.maindata.TorrentsCollector
import org.junit.jupiter.api.Test

internal class MaindataCollectorContainerSettingsTest {
  @Test
  fun defaultSettings_containsAllTorrentsCollectors() {
    val settings = MaindataCollectorContainerSettings()

    assertThat(settings.torrentsCollectors).hasSize(TorrentsCollector.values().size)
  }

  @Test
  fun defaultSettings_containsAllServerStateCollectors() {
    val settings = MaindataCollectorContainerSettings()

    assertThat(settings.serverStateCollectors).hasSize(ServerStateCollector.values().size)
  }
}
