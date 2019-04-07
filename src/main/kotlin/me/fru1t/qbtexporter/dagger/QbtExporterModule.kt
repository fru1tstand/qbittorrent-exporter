package me.fru1t.qbtexporter.dagger
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import javax.inject.Singleton

/**
 * Provides initial concrete dependencies to the QbtExporter if they can't automatically be
 * instantiated by dagger.
 */
@Module
class QbtExporterModule {
  @Singleton
  @Provides
  fun provideGson(): Gson = GsonBuilder().setPrettyPrinting().create()

  @Singleton
  @Provides
  fun provideHttpClient(): HttpClient = HttpClient(Apache)
}
