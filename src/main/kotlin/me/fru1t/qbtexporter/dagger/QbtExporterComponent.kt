package me.fru1t.qbtexporter.dagger

import dagger.Component
import me.fru1t.qbtexporter.QbtExporter
import javax.inject.Singleton

/** Starts the dependency graph for QbtExporter. */
@Singleton
@Component(modules = [QbtExporterModule::class, QbtExporterInterfacesModule::class])
interface QbtExporterComponent {
  fun inject(qbtExporter: QbtExporter)
}
