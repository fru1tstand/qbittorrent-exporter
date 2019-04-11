package me.fru1t.qbtexporter.collector.maindata

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
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
            dateAddedUnixTimestamp = 1,
            uploadPayloadRateBytesPerSecond = 2
          )
        ),
        Pair(
          "torrent2hash",
          Torrent(
            displayName = "torrent2",
            dateAddedUnixTimestamp = 1000,
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
    private fun labelOf(torrentPair: Pair<String, Torrent>, torrentsCollector: TorrentsCollector): String =
      "qbt_torrent_${torrentsCollector.name.toLowerCase()}{hash=\"${torrentPair.first}\"," +
          "name=\"${torrentPair.second.displayName!!}\"}"

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
  fun collect_dateAddedUnixTimestamp() {
    assertOutput(TorrentsCollector.DATE_ADDED_UNIX_TIMESTAMP, 1, 1000)
  }

  @Test
  fun collect_uploadPayloadRateBytesPerSecond() {
    assertOutput(TorrentsCollector.UPLOAD_PAYLOAD_RATE_BYTES_PER_SECOND, 2, null)
  }
}
