package me.fru1t.qbtexporter.collector.maindata

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.ServerState
import org.junit.jupiter.api.Test

internal class ServerStateCollectorTest {
  private companion object {
    private val TEST_SERVER_STATE = ServerState(
      allTimeDownloadedBytes = 1,
      allTimeUploadedBytes = 2
    )
    private val TEST_DATA = Maindata(serverState = TEST_SERVER_STATE)

    /** Returns the metric name for the [serverStateCollector]. */
    private fun metricNameOf(serverStateCollector: ServerStateCollector): String =
        "qbt_server_state_${serverStateCollector.name.toLowerCase()}"

    /**
     * Asserts that when passing [TEST_DATA] into the [serverStateCollector], the [expectedOutput]
     * is produced.
     */
    private fun assertOutput(serverStateCollector: ServerStateCollector, expectedOutput: Number) {
      val resultInternalMetric = serverStateCollector.collect(TEST_DATA).toString().lines().last()

      assertThat(resultInternalMetric)
        .isEqualTo("${metricNameOf(serverStateCollector)} $expectedOutput")
    }
  }

  @Test
  fun collect_allTimeDownloadBytes() {
    assertOutput(
      ServerStateCollector.ALL_TIME_DOWNLOAD_BYTES, TEST_SERVER_STATE.allTimeDownloadedBytes!!)
  }

  @Test
  fun collect_allTimeUploadBytes() {
    assertOutput(
      ServerStateCollector.ALL_TIME_UPLOAD_BYTES, TEST_SERVER_STATE.allTimeUploadedBytes!!)
  }
}
