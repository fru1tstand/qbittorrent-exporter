package me.fru1t.qbtexporter.exporter.impl

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.engine.cio.CIO
import io.ktor.client.response.readText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import me.fru1t.qbtexporter.collector.CollectorSettingsUtils
import me.fru1t.qbtexporter.collector.maindata.ServerStateCollector
import me.fru1t.qbtexporter.exporter.ExporterServerSettings
import me.fru1t.qbtexporter.kotlin.testing.TestLazyRelay
import me.fru1t.qbtexporter.qbt.api.QbtApi
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.SettingsManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class ExporterServerImplTest {
  private companion object {
    private const val TEST_PORT = 9562
    private const val TEST_HOST = "127.0.0.4"
    private const val TEST_BASE_URL = "http://$TEST_HOST:$TEST_PORT"

    private val TEST_SETTINGS = Settings(
      exporterServerSettings = ExporterServerSettings(
        port = TEST_PORT,
        host = TEST_HOST
      )
    )
  }

  @Mock private lateinit var mockQbtApi: QbtApi
  @Mock private lateinit var mockCollectorSettingsUtils: CollectorSettingsUtils
  @Mock private lateinit var mockSettingsManager: SettingsManager
  private lateinit var exporterServerImpl: ExporterServerImpl

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    whenever(mockSettingsManager.getSettingsRelay()).thenReturn(TestLazyRelay.of(TEST_SETTINGS))
    exporterServerImpl =
      ExporterServerImpl(
        qbtApi = mockQbtApi,
        collectorSettingsUtils = mockCollectorSettingsUtils,
        settingsManager = mockSettingsManager
      )
  }

  @Test
  fun runBlocking_root() = testDuringServerLifecycle {
    val responseText =
      runBlocking { HttpClient(CIO).call(TEST_BASE_URL).response.readText() }
    assertThat(responseText).isEqualTo("<a href=\"/metrics\">metrics</a>")
  }

  @Test
  fun runBlocking_metrics() = testDuringServerLifecycle {
    val testData = Maindata()
    whenever(mockQbtApi.fetchMaindata()).thenReturn(testData)
    whenever(mockCollectorSettingsUtils.getEnabledMaindataCollectors())
      .thenReturn(listOf(ServerStateCollector.ALL_TIME_DOWNLOAD_BYTES))

    val response = runBlocking { HttpClient(CIO).call("$TEST_BASE_URL/metrics").response }
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
