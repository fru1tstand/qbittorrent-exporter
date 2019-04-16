package me.fru1t.qbtexporter

import me.fru1t.qbtexporter.dagger.DaggerQbtExporterComponent
import me.fru1t.qbtexporter.exporter.ExporterServer
import me.fru1t.qbtexporter.logger.Logger
import javax.inject.Inject

fun main() {
  QbtExporter().run()
}

class QbtExporter : Runnable {
  @Inject lateinit var exporterServer: ExporterServer
  @Inject lateinit var logger: Logger

  init {
    DaggerQbtExporterComponent.builder().build().inject(this)
  }

  override fun run() {
    exporterServer.start()
    while (!Thread.interrupted()) {
      Thread.sleep(50)
    }
    exporterServer.stop()
    logger.i("Server stopped gracefully. Bye!")
  }
}
