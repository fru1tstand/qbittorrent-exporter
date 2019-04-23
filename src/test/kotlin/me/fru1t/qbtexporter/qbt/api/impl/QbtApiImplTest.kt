package me.fru1t.qbtexporter.qbt.api.impl

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import me.fru1t.qbtexporter.kotlin.testing.TestLazyRelay
import me.fru1t.qbtexporter.qbt.QbtSettings
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.SettingsManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class QbtApiImplTest {
  private companion object {
    private val TEST_SETTINGS =
      Settings(qbtSettings = QbtSettings(webUiAddress = "http://test.web.address"))
    private val TEST_MAINDATA = Maindata(null, 123123, null, null, null)
  }

  @Mock private lateinit var mockSettingsManager: SettingsManager
  private lateinit var gson: Gson
  private lateinit var httpClient: HttpClient
  private lateinit var api: QbtApiImpl

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    whenever(mockSettingsManager.getSettingsRelay()).thenReturn(TestLazyRelay.of(TEST_SETTINGS))
    gson = Gson()

    val mockHttpEngine = MockEngine {
      when (url.toString()) {
        TEST_SETTINGS.qbtSettings!!.webUiAddress!! + QbtApiImpl.MAINDATA_PATH ->
          MockHttpResponse(
            call,
            HttpStatusCode.OK,
            ByteReadChannel(gson.toJson(TEST_MAINDATA)),
            headersOf("Content-Type", "text/json")
          )
        else ->
          error(
            "Test received $url when it was expecting " +
                TEST_SETTINGS.qbtSettings!!.webUiAddress!! + QbtApiImpl.MAINDATA_PATH
          )
      }
    }
    httpClient = HttpClient(mockHttpEngine)

    api = QbtApiImpl(gson, httpClient, mockSettingsManager)
  }

  @Test
  fun fetchMainData() {
    val resultMaindata = api.fetchMaindata()

    assertThat(resultMaindata.responseId!!).isEqualTo(TEST_MAINDATA.responseId)
  }
}
