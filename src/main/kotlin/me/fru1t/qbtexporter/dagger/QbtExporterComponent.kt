package me.fru1t.qbtexporter.dagger

import dagger.Component

/** Starts the dependency graph for QbtExporter. */
@Component(modules = [QbtExporterModule::class])
interface QbtExporterComponent
