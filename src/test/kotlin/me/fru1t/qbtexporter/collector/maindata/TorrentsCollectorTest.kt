package me.fru1t.qbtexporter.collector.maindata

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import me.fru1t.qbtexporter.collector.MaindataCollectorContainerSettings
import me.fru1t.qbtexporter.prometheus.metric.multimetric.MetricLabel
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.torrents.Torrent
import org.junit.jupiter.api.Test

internal class TorrentsCollectorTest {
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
            downloadRemainingBytes = 0,
            completedBytes = 0,
            downloadPayloadRateBytesPerSecond = 0,
            downloadTotalBytes = 0,
            downloadSessionBytes = 0,
            seedersAvailable = 0,
            seedersConnected = 0,
            leechersAvailable = 0,
            leechersConnected = 0,
            ratio = 0.0,
            activeTimeSeconds = 0,
            uploadTotalBytes = 0,
            uploadSessionBytes = 0,
            uploadPayloadRateBytesPerSecond = 0
          )
        )
      )
    )

    /**
     * Collects on [torrentsCollector] and returns only the internal metrics portion of the metrics.
     */
    private fun collectAndTrim(torrentsCollector: TorrentsCollector): String =
      torrentsCollector.collect(TEST_DATA).toString()
        .lines()
        .filterIndexed { index, _ -> index > 1 }
        .joinToString(separator = "\n")

    /** Creates the metric name including label for the given torrent and collector.*/
    private fun labelOf(
      torrentPair: Pair<String, Torrent>,
      torrentsCollector: TorrentsCollector
    ): String {
      val label =
        MetricLabel.Builder()
          .addLabel("name", torrentPair.second.displayName!!)
          .addLabel("hash", torrentPair.first)
          .build()
      return "qbt_torrent_${torrentsCollector.name.toLowerCase()}{$label}"
    }

    /**
     * Asserts that when passing [TEST_DATA] into the [torrentsCollector], the [expectedOutputs]
     * are produced.
     *
     * [expectedOutputs] should be a list of outputs, one entry per torrent within [TEST_DATA], in
     * the order in which they're defined. If the torrent shouldn't produce an output line for the
     * [torrentsCollector], the [expectedOutputs] value should be `null`.
     */
    private fun assertOutput(torrentsCollector: TorrentsCollector, vararg expectedOutputs: Number?) {
      assertWithMessage(
        "Test is not set up correctly. The number of expected outputs needs to equal the number " +
            "of inputs. To expect no output, pass null.")
        .that(TEST_DATA.torrents!!.size)
        .isEqualTo(expectedOutputs.size)

      val resultLines = collectAndTrim(torrentsCollector)

      val expectedLines = ArrayList<String>()
      TEST_DATA.torrents!!.toList().forEachIndexed {
        index, torrentPair ->
        if (expectedOutputs[index] != null) {
          expectedLines.add("${labelOf(torrentPair, torrentsCollector)} ${expectedOutputs[index]}")
        }
      }

      assertThat(resultLines).isEqualTo(expectedLines.joinToString(separator = "\n"))
    }
  }

  @Test
  fun container_collect() {
    // Enable all collectors
    val settings = MaindataCollectorContainerSettings()
    settings.torrentsCollectors!!.values.forEach { it.enabled = true }

    val result = TorrentsCollector.collect(settings, TEST_DATA)

    assertThat(result).hasSize(TorrentsCollector.values().size)
  }

  @Test
  fun collect_downloadRemainingBytes() {
    assertOutput(TorrentsCollector.DOWNLOAD_REMAINING_BYTES, 1, null)
  }

  @Test
  fun collect_completedBytes() {
    assertOutput(TorrentsCollector.COMPLETED_BYTES, 2, 0)
  }

  @Test
  fun collect_downloadPayloadRateBytesPerSecond() {
    assertOutput(TorrentsCollector.DOWNLOAD_PAYLOAD_RATE_BYTES_PER_SECOND, 3, null)
  }

  @Test
  fun collect_downloadTotalBytes() {
    assertOutput(TorrentsCollector.DOWNLOAD_TOTAL_BYTES, 4, 0)
  }

  @Test
  fun collect_downloadSessionBytes() {
    assertOutput(TorrentsCollector.DOWNLOAD_SESSION_BYTES, 5, null)
  }

  @Test
  fun collect_seedersAvailable() {
    assertOutput(TorrentsCollector.SEEDERS_AVAILABLE, 6, null)
  }

  @Test
  fun collect_seedersConnected() {
    assertOutput(TorrentsCollector.SEEDERS_CONNECTED, 7, null)
  }

  @Test
  fun collect_leechersAvailable() {
    assertOutput(TorrentsCollector.LEECHERS_AVAILABLE, 8, null)
  }

  @Test
  fun collect_leechersConnected() {
    assertOutput(TorrentsCollector.LEECHERS_CONNECTED, 9, null)
  }

  @Test
  fun collect_ratio() {
    assertOutput(TorrentsCollector.RATIO, 10.10, 0.0)
  }

  @Test
  fun collect_activeTimeSeconds() {
    assertOutput(TorrentsCollector.ACTIVE_TIME_SECONDS, 11, 0)
  }

  @Test
  fun collect_uploadTotalBytes() {
    assertOutput(TorrentsCollector.UPLOAD_TOTAL_BYTES, 12, 0)
  }

  @Test
  fun collect_uploadSessionBytes() {
    assertOutput(TorrentsCollector.UPLOAD_SESSION_BYTES, 13, null)
  }

  @Test
  fun collect_uploadPayloadRateBytesPerSecond() {
    assertOutput(TorrentsCollector.UPLOAD_PAYLOAD_RATE_BYTES_PER_SECOND, 14, null)
  }
}
