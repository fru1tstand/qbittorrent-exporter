package me.fru1t.qbtexporter.exporter

/** Controls the exporter server which handles http requests from prometheus. */
interface ExporterServer {
  /** Starts the server on a new thread and returns immediately. */
  fun start()

  /** Stops the server if it's running. */
  fun stop()
}
