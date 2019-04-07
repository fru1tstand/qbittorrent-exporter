package me.fru1t.qbtexporter.dagger
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import me.fru1t.qbtexporter.logger.Logger
import me.fru1t.qbtexporter.logger.impl.ConsoleLogger
import me.fru1t.qbtexporter.settings.impl.SettingsManagerImpl
import javax.inject.Named
import javax.inject.Singleton

/**
 * Provides initial concrete dependencies to the QbtExporter if they can't automatically be
 * instantiated by dagger.
 */
@Module
class QbtExporterModule {
  @Provides
  @Singleton
  fun provideGson(): Gson = GsonBuilder().setPrettyPrinting().create()

  @Provides
  @Singleton
  fun provideHttpClient(): HttpClient = HttpClient(Apache)

  @Provides
  @Singleton
  fun provideLogger(): Logger = ConsoleLogger(ConsoleLogger.LOG_LEVEL_INFO)

  @Provides
  @Named(SettingsManagerImpl.NAMED_FILE_PATH)
  fun provideSettingsManagerImplFilePath(): String = ""
}
