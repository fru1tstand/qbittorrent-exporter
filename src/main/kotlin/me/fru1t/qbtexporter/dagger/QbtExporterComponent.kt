package me.fru1t.qbtexporter.dagger

import dagger.BindsInstance
import dagger.Component
import me.fru1t.qbtexporter.QbtExporter
import javax.inject.Named
import javax.inject.Singleton

/** Starts the dependency graph for QbtExporter. */
@Singleton
@Component(modules = [QbtExporterModule::class, QbtExporterInterfacesModule::class])
interface QbtExporterComponent {
  companion object {
    const val CLI_ARGS_NAME = "QbtExporterComponent_cliArgs"
  }

  fun init(): QbtExporter

  /** Entry point to provide things to the dependency graph before it's created. */
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun provideCliArgs(@Named(CLI_ARGS_NAME) args: Array<String>): Builder

    fun build(): QbtExporterComponent
  }
}
