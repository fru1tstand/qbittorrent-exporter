package me.fru1t.qbtexporter.qbt.api.impl

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.response.readText
import kotlinx.coroutines.runBlocking
import me.fru1t.qbtexporter.kotlin.LazyRelay
import me.fru1t.qbtexporter.qbt.api.QbtApi
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.SettingsManager
import javax.inject.Inject

/** Implementation of [QbtApi]. */
class QbtApiImpl @Inject constructor(
  private val gson: Gson,
  private val httpClient: HttpClient,
  settingsManager: SettingsManager
) : QbtApi {
  internal companion object {
    const val MAINDATA_PATH = "/api/v2/sync/maindata"
  }

  private val settingsRelay: LazyRelay<Settings> = settingsManager.getSettingsRelay()

  override fun fetchMaindata(): Maindata {
    return gson.fromJson(
      runBlocking {
        httpClient.call(settingsRelay.poll().qbtSettings?.webUiAddress + MAINDATA_PATH)
          .response.readText()
      },
      Maindata::class.java
    )
  }
}
