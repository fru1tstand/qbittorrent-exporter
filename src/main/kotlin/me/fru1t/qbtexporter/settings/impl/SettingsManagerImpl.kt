package me.fru1t.qbtexporter.settings.impl

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import me.fru1t.qbtexporter.logger.Logger
import me.fru1t.qbtexporter.settings.Settings
import me.fru1t.qbtexporter.settings.SettingsManager
import java.io.File
import java.io.IOException
import java.lang.RuntimeException
import javax.inject.Inject

/** Default implementation of [SettingsManager]. */
class SettingsManagerImpl @Inject constructor(private val gson: Gson, private val logger: Logger) :
  SettingsManager {
  internal companion object {
    internal const val DEFAULT_SETTINGS_FILE_LOCATION: String = "qbt-exporter-settings.json"
    internal const val EXAMPLE_SETTINGS_FILE_LOCATION: String = "example-qbt-exporter-settings.json"

    private val SETTINGS_FILE: File = File(DEFAULT_SETTINGS_FILE_LOCATION)
  }

  private var settings: Settings? = null

  override fun save() {
    // Do nothing if the settings haven't even been loaded
    if (settings == null) {
      return
    }

    SETTINGS_FILE.writeText(gson.toJson(settings!!))
  }

  override fun get(): Settings {
    if (settings == null) {
      if (!SETTINGS_FILE.exists()) {
        logger.i("No settings file found, creating one.")
        settings = Settings()
        save()
      } else {
        logger.i("Settings found, loading from $DEFAULT_SETTINGS_FILE_LOCATION")
        loadSettingsFromFile()
      }
    }

    return settings!!
  }

  private fun loadSettingsFromFile() {
    if (!SETTINGS_FILE.canRead() && !SETTINGS_FILE.canWrite()) {
      throw RuntimeException(
        "I need read and write access to $DEFAULT_SETTINGS_FILE_LOCATION to work."
      )
    }

    try {
      settings = gson.fromJson(SETTINGS_FILE.reader(), Settings::class.java)
    } catch (e: JsonSyntaxException) {
      logger.e("Failed to read the settings file at $DEFAULT_SETTINGS_FILE_LOCATION.")
      writeExampleSettings()
      logger.i("But I'm still gonna crash so you can figure out the issue.")
      throw e
    }
  }

  /** Writes the default settings to an example file. */
  private fun writeExampleSettings() {
    val exampleSettingsFile = File(EXAMPLE_SETTINGS_FILE_LOCATION)
    if (!exampleSettingsFile.canWrite()) {
      logger.i(
        "I can't even write an example settings file for you as I don't have write permissions " +
            "to $EXAMPLE_SETTINGS_FILE_LOCATION."
      )
    }

    logger.i("Trying to write an example settings file at $EXAMPLE_SETTINGS_FILE_LOCATION.")
    try {
      exampleSettingsFile.writeText(gson.toJson(Settings()))
      logger.i("Success!")
    } catch (e: IOException) {
      logger.e("Nevermind. Something bad happened while writing the example settings:")
      throw e
    }
  }
}
