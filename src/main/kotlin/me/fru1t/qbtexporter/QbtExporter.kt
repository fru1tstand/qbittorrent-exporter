package me.fru1t.qbtexporter

import com.google.gson.Gson
import me.fru1t.qbtexporter.dagger.DaggerQbtExporterComponent
import me.fru1t.qbtexporter.qbt.response.Maindata
import java.io.File
import java.lang.RuntimeException
import javax.inject.Inject

fun main(args: Array<String>) {
  QbtExporter().run()
}

class QbtExporter : Runnable {
  @Inject lateinit var gson: Gson

  init {
    DaggerQbtExporterComponent.builder().build().inject(this)
  }

  override fun run() {
    println("Hello world!")

    val json = File("testdata.json")
    if (!json.exists()) {
      throw RuntimeException("Can't find our test data")
    }

    val result = gson.fromJson(json.reader(), Maindata::class.java)

    println("Printing json:")
    println(result.toString())
  }
}
