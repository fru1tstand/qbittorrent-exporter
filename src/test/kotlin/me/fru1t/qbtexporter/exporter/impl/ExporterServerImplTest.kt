package me.fru1t.qbtexporter.exporter.impl

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.engine.apache.Apache
import io.ktor.client.response.readText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import me.fru1t.qbtexporter.collector.CollectorSettingsUtils
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.qbt.api.QbtApi
import me.fru1t.qbtexporter.qbt.response.Maindata
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class ExporterServerImplTest {
  @Mock private lateinit var mockQbtApi: QbtApi
  @Mock private lateinit var mockCollectorSettingsUtils: CollectorSettingsUtils
  private lateinit var exporterServerImpl: ExporterServerImpl

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    exporterServerImpl =
      ExporterServerImpl(qbtApi = mockQbtApi, collectorSettingsUtils = mockCollectorSettingsUtils)
  }

  @Test
  fun runBlocking_root() = testDuringServerLifecycle {
    val responseText =
      runBlocking { HttpClient(Apache).call("http://localhost:9561").response.readText() }
    assertThat(responseText).isEqualTo("<a href=\"/metrics\">metrics</a>")
  }

  @Test
  fun runBlocking_metrics() = testDuringServerLifecycle {
    val testData = Maindata()
    whenever(mockQbtApi.fetchMaindata()).thenReturn(testData)
    whenever(mockCollectorSettingsUtils.getEnabledMaindataCollectors())
      .thenReturn(listOf(ServerStateCollector.ALL_TIME_DOWNLOAD_BYTES))

    val response = runBlocking { HttpClient(Apache).call("http://localhost:9561/metrics").response }
    assertThat(response.status).isEqualTo(HttpStatusCode.OK)
    assertThat(runBlocking { response.readText() })
      .isEqualTo(ServerStateCollector.ALL_TIME_DOWNLOAD_BYTES.collect(testData).toString())
  }

  private fun testDuringServerLifecycle(test: () -> Unit) {
    // If starting the server produced an error, immediately error
    val startResult = runCatching { exporterServerImpl.start() }
    assertThat(startResult.exceptionOrNull()).isNull()

    test()
    exporterServerImpl.stop()
  }
}
