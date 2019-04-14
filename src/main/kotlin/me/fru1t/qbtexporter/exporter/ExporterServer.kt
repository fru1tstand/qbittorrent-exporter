package me.fru1t.qbtexporter.exporter

/** Controls the exporter server which handles http requests from prometheus. */
interface ExporterServer {
  /** Start and block the current thread to run the server. */
  fun runBlocking()
}
