package me.fru1t.qbtexporter.collector.maindata

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.collector.settings.MaindataCollectorContainerSettings
import me.fru1t.qbtexporter.collector.settings.maindata.AggregateTorrentCollectorSettings
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.torrents.Torrent
import org.junit.jupiter.api.Test

internal class AggregateTorrentCollectorTest {
  private companion object {
    private val TEST_DATA = Maindata(
      torrents = mapOf(
        Pair(
          "torrent1hash",
          Torrent(
            displayName = "torrent1",
            downloadRemainingBytes = 1,
            completedBytes = 2,
            downloadPayloadRateBytesPerSecond = 3,
            downloadTotalBytes = 4,
            downloadSessionBytes = 5,
            seedersAvailable = 6,
            leechersAvailable = 7,
            seedersConnected = 8,
            leechersConnected = 9,
            activeTimeSeconds = 10,
            sizeWantedBytes = 11,
            sizeTotalBytes = 12,
            uploadTotalBytes = 13,
            uploadSessionBytes = 14,
            uploadPayloadRateBytesPerSecond = 15
          )
        ),
        Pair(
          "torrent2hash",
          Torrent(
            displayName = "torrent2",
            downloadRemainingBytes = null,
            completedBytes = null,
            downloadPayloadRateBytesPerSecond = null,
            downloadTotalBytes = null,
            downloadSessionBytes = null,
            seedersAvailable = null,
            seedersConnected = null,
            leechersAvailable = null,
            leechersConnected = null,
            sizeWantedBytes = null,
            sizeTotalBytes = null,
            uploadTotalBytes = null,
            uploadSessionBytes = null,
            uploadPayloadRateBytesPerSecond = null
          )
        )
      )
    )

    private val TEST_SETTINGS = AggregateTorrentCollectorSettings(all = true)

    /**
     * Asserts that when passing [TEST_DATA] into the [aggregateTorrentCollector], the
     * [expectedSpecialAllOutput] is produced.
     */
    private fun assertOutput(
      aggregateTorrentCollector: AggregateTorrentCollector,
      expectedSpecialAllOutput: Number
    ) {
      val resultInternalMetric =
        aggregateTorrentCollector.collect(TEST_SETTINGS, TEST_DATA)
          .toString()
          .lines()
          .filterIndexed { index, _ -> index > 1 }
          .joinToString(separator = "\n")

      val baseMetricName = "qbt_aggregate_torrent_${aggregateTorrentCollector.name.toLowerCase()}"
      assertThat(resultInternalMetric)
        .isEqualTo("$baseMetricName{special=\"all\"} $expectedSpecialAllOutput")
    }
  }

  @Test
  fun collect() {
    // Enable all collectors
    val settings = MaindataCollectorContainerSettings()
    settings.aggregateTorrentCollectors!!.values.forEach { it.all = true }

    val result = AggregateTorrentCollector.collect(settings, TEST_DATA)

    assertThat(result).hasSize(AggregateTorrentCollector.values().size)
  }

  @Test
  fun downloadRemainingBytes() {
    assertOutput(AggregateTorrentCollector.DOWNLOAD_REMAINING_BYTES, 1)
  }

  @Test
  fun completedBytes() {
    assertOutput(AggregateTorrentCollector.COMPLETED_BYTES, 2)
  }

  @Test
  fun downloadPayloadRateBytesPerSecond() {
    assertOutput(AggregateTorrentCollector.DOWNLOAD_PAYLOAD_RATE_BYTES_PER_SECOND, 3)
  }

  @Test
  fun downloadTotalBytes() {
    assertOutput(AggregateTorrentCollector.DOWNLOAD_TOTAL_BYTES, 4)
  }

  @Test
  fun downloadSessionBytes() {
    assertOutput(AggregateTorrentCollector.DOWNLOAD_SESSION_BYTES, 5)
  }

  @Test
  fun seedersAvailable() {
    assertOutput(AggregateTorrentCollector.SEEDERS_AVAILABLE, 6)
  }

  @Test
  fun leechersAvailable() {
    assertOutput(AggregateTorrentCollector.LEECHERS_AVAILABLE, 7)
  }

  @Test
  fun seedersConnected() {
    assertOutput(AggregateTorrentCollector.SEEDERS_CONNECTED, 8)
  }

  @Test
  fun leechersConnected() {
    assertOutput(AggregateTorrentCollector.LEECHERS_CONNECTED, 9)
  }

  @Test
  fun activeTimeSeconds() {
    assertOutput(AggregateTorrentCollector.ACTIVE_TIME_SECONDS, 10)
  }

  @Test
  fun sizeWantedBytes() {
    assertOutput(AggregateTorrentCollector.SIZE_WANTED_BYTES, 11)
  }

  @Test
  fun sizeTotalBytes() {
    assertOutput(AggregateTorrentCollector.SIZE_TOTAL_BYTES, 12)
  }

  @Test
  fun uploadTotalBytes() {
    assertOutput(AggregateTorrentCollector.UPLOAD_TOTAL_BYTES, 13)
  }

  @Test
  fun uploadSessionBytes() {
    assertOutput(AggregateTorrentCollector.UPLOAD_SESSION_BYTES, 14)
  }

  @Test
  fun uploadPayloadRateBytesPerSecond() {
    assertOutput(AggregateTorrentCollector.UPLOAD_PAYLOAD_RATE_BYTES_PER_SECOND, 15)
  }

  @Test
  fun torrentCount() {
    assertOutput(AggregateTorrentCollector.TORRENT_COUNT, TEST_DATA.torrents!!.size)
  }
}
