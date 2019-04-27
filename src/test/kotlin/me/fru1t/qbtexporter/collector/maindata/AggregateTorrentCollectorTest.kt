package me.fru1t.qbtexporter.collector.maindata

import com.google.common.truth.Truth.assertThat
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
            seedersConnected = 7,
            leechersAvailable = 8,
            leechersConnected = 9,
            ratio = 10.10,
            activeTimeSeconds = 11,
            uploadTotalBytes = 12,
            uploadSessionBytes = 13,
            uploadPayloadRateBytesPerSecond = 14
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
            ratio = null,
            activeTimeSeconds = null,
            uploadTotalBytes = null,
            uploadSessionBytes = null,
            uploadPayloadRateBytesPerSecond = null
          )
        )
      )
    )

    /** Returns the metric name for the [serverStateCollector]. */
    private fun metricNameOf(aggregateTorrentCollector: AggregateTorrentCollector): String =
      "qbt_aggregate_torrent_${aggregateTorrentCollector.name.toLowerCase()}"

    /**
     * Asserts that when passing [TEST_DATA] into the [aggregateTorrentCollector], the
     * [expectedOutput] is produced.
     */
    private fun assertOutput(
      aggregateTorrentCollector: AggregateTorrentCollector,
      expectedOutput: Number
    ) {
      val resultInternalMetric =
        aggregateTorrentCollector.collect(TEST_DATA).toString().lines().last()

      assertThat(resultInternalMetric)
        .isEqualTo("${metricNameOf(aggregateTorrentCollector)} $expectedOutput")
    }
  }

  @Test
  fun downloadRemainingBytes() {
    assertOutput(AggregateTorrentCollector.DOWNLOAD_REMAINING_BYTES, 1)
  }
}
