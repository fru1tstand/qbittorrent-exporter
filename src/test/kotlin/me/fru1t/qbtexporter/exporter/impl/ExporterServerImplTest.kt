package me.fru1t.qbtexporter.exporter.impl

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.engine.apache.Apache
import io.ktor.client.response.readText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import me.fru1t.qbtexporter.collector.CollectorSettings
import me.fru1t.qbtexporter.collector.CollectorSettingsHelper
import me.fru1t.qbtexporter.qbt.api.QbtApi
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.SettingsManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class ExporterServerImplTest {
  @Mock private lateinit var mockQbtApi: QbtApi
  @Mock private lateinit var mockSettingsManager: SettingsManager
  private lateinit var exporterServerImpl: ExporterServerImpl

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    exporterServerImpl =
      ExporterServerImpl(qbtApi = mockQbtApi, settingsManager = mockSettingsManager)
  }

  @Test
  fun runBlocking_root() = testDuringServerLifecycle {
    val responseText =
      runBlocking { HttpClient(Apache).call("http://localhost:9561").response.readText() }
    assertThat(responseText).isEqualTo("<a href=\"/metrics\">metrics</a>")
  }

  @Test
  fun runBlocking_metrics() = testDuringServerLifecycle {
    whenever(mockQbtApi.fetchMaindata()).thenReturn(Maindata())

    // Set up mock settings manager to return at least one enabled collector
    val collectorSettings = CollectorSettingsHelper.createDefaultSettings()
    val entryInCollectorSettings = collectorSettings.entries.first()
    val enabledMaindataCollectors =
      mapOf(Pair(entryInCollectorSettings.key, entryInCollectorSettings.value.mapValues { true }))
    val settings =
      Settings(
        collectorSettings = CollectorSettings(
          maindataCollectors = enabledMaindataCollectors))
    whenever(mockSettingsManager.get()).thenReturn(settings)

    val response = runBlocking { HttpClient(Apache).call("http://localhost:9561/metrics").response }
    assertThat(response.status).isEqualTo(HttpStatusCode.OK)
    assertThat(runBlocking { response.readText() }).isNotEmpty()
  }

  private fun testDuringServerLifecycle(test: () -> Unit) {
    // If starting the server produced an error, immediately error
    val startResult = runCatching { exporterServerImpl.start() }
    assertThat(startResult.exceptionOrNull()).isNull()

    test()
    exporterServerImpl.stop()
  }
}
