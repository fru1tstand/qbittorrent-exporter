package me.fru1t.qbtexporter

import me.fru1t.qbtexporter.dagger.DaggerQbtExporterComponent

fun main(args: Array<String>) {
  QbtExporter().run()
}

class QbtExporter : Runnable {
  init {
    DaggerQbtExporterComponent.create()
  }

  override fun run() {
    println("Hello world!")
  }
}
