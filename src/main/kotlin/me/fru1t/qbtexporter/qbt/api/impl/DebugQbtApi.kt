package me.fru1t.qbtexporter.qbt.api.impl

import com.google.gson.Gson
import me.fru1t.qbtexporter.qbt.api.QbtApi
import me.fru1t.qbtexporter.qbt.response.Maindata
import java.io.File
import javax.inject.Inject

/** A debug implementation of [QbtApi] that reads json from disk. */
class DebugQbtApi @Inject constructor(private val gson: Gson) : QbtApi {
  override fun fetchMaindata(): Maindata {
    return gson.fromJson(File("ignored/maindata.json").readText(), Maindata::class.java)
  }
}
