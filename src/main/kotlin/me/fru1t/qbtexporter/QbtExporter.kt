package me.fru1t.qbtexporter

import me.fru1t.qbtexporter.dagger.DaggerQbtExporterComponent
import me.fru1t.qbtexporter.qbt.api.QbtApi
import javax.inject.Inject

fun main() {
  QbtExporter().run()
}

class QbtExporter : Runnable {
  @Inject lateinit var qbtApi: QbtApi

  init {
    DaggerQbtExporterComponent.builder().build().inject(this)
  }

  override fun run() {
    println(qbtApi.fetchMaindata().toString())
  }
}
