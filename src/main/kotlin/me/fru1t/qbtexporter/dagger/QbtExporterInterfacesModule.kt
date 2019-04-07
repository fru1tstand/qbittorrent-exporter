package me.fru1t.qbtexporter.dagger

import dagger.Binds
import dagger.Module
import me.fru1t.qbtexporter.qbt.api.QbtApi
import me.fru1t.qbtexporter.qbt.api.impl.QbtApiImpl
import me.fru1t.qbtexporter.settings.SettingsManager
import me.fru1t.qbtexporter.settings.impl.SettingsManagerImpl
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Connects together interfaces to their implementations when the interface is a dependency within
 * another class. This is how dagger knows how to provide the concrete object when the interface
 * is required within an [Inject] parameter or field.
 */
@Module
interface QbtExporterInterfacesModule {
  @Binds @Singleton fun bindSettingsManager(impl: SettingsManagerImpl): SettingsManager
  @Binds @Singleton fun bindQbtApi(impl: QbtApiImpl): QbtApi
}
