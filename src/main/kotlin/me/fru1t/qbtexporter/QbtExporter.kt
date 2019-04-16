package me.fru1t.qbtexporter

import me.fru1t.qbtexporter.dagger.DaggerQbtExporterComponent
import me.fru1t.qbtexporter.exporter.ExporterServer
import javax.inject.Inject

fun main() {
  QbtExporter().run()
}

class QbtExporter : Runnable {
  @Inject lateinit var exporterServer: ExporterServer

  init {
    DaggerQbtExporterComponent.builder().build().inject(this)
  }

  override fun run() {
    exporterServer.start()
    while (!Thread.interrupted()) {
      Thread.sleep(50)
    }
    exporterServer.stop()
    println("Stopped server gracefully. Bye!")
  }
}
