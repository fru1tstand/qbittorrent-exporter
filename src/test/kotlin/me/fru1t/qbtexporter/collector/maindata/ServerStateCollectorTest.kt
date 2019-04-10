package me.fru1t.qbtexporter.collector.maindata

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.prometheus.metric.SingleMetric
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.ServerState
import org.junit.jupiter.api.Test

internal class ServerStateCollectorTest {
  private companion object {
    private val TEST_DATA = Maindata(
      serverState = ServerState(
        allTimeDownloadedBytes = 1,
        allTimeUploadedBytes = 2
      )
    )
  }

  @Test
  fun collect() {
    val serverState = TEST_DATA.serverState!!

    collectAndAssertValue(
      ServerStateCollector.ALL_TIME_DOWNLOAD_BYTES, serverState.allTimeDownloadedBytes
    )
    collectAndAssertValue(
      ServerStateCollector.ALL_TIME_UPLOAD_BYTES, serverState.allTimeUploadedBytes
    )
  }

  private fun collectAndAssertValue(serverStateCollector: ServerStateCollector, value: Number?) {
    val metric = serverStateCollector.collect(TEST_DATA) as SingleMetric
    assertThat(metric.value).isEqualTo(value)
  }
}
