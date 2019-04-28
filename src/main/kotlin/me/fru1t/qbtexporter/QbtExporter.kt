package me.fru1t.qbtexporter

import me.fru1t.qbtexporter.dagger.DaggerQbtExporterComponent
import me.fru1t.qbtexporter.exporter.ExporterServer
import me.fru1t.qbtexporter.logger.Logger
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

fun main(args: Array<String>) {
  val cdl = CountDownLatch(1)
  Runtime.getRuntime().addShutdownHook(Thread { cdl.countDown() })
  DaggerQbtExporterComponent.builder().provideCliArgs(args).build().init().start(cdl)
}

class QbtExporter @Inject constructor(
  private val exporterServer: ExporterServer,
  private val logger: Logger
) {
  fun start(cdl: CountDownLatch) {
    exporterServer.start()
    cdl.await()
    exporterServer.stop()
    logger.i("Server stopped gracefully. Bye!")
  }
}
