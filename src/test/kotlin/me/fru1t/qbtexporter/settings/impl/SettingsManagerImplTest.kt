package me.fru1t.qbtexporter.settings.impl

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import me.fru1t.qbtexporter.kotlin.LazyRelayFactory
import me.fru1t.qbtexporter.logger.Logger
import me.fru1t.qbtexporter.settings.Settings
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.File

internal class SettingsManagerImplTest {
  private companion object {
    private const val TESTING_DIRECTORY = "test-out/"
    private val SETTINGS_FILE =
      File(TESTING_DIRECTORY + SettingsManagerImpl.DEFAULT_SETTINGS_FILE_LOCATION)
    private val EXAMPLE_SETTINGS_FILE =
      File(TESTING_DIRECTORY + SettingsManagerImpl.EXAMPLE_SETTINGS_FILE_LOCATION)
  }

  private lateinit var gson: Gson
  private lateinit var manager: SettingsManagerImpl

  @Mock private lateinit var mockLogger: Logger
  @Mock private lateinit var mockLazyRelayFactory: LazyRelayFactory
  private val relayCalculateCaptor = argumentCaptor<() -> Settings>()
  private val relaySignalCaptor = argumentCaptor<() -> Any?>()

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    gson = Gson()
    manager =
      SettingsManagerImpl(
        gson = gson,
        logger = mockLogger,
        settingsFilePath = TESTING_DIRECTORY,
        lazyRelayFactory = mockLazyRelayFactory
      )
  }

  @AfterEach
  fun tearDown() {
    SETTINGS_FILE.delete()
    EXAMPLE_SETTINGS_FILE.delete()
    File(TESTING_DIRECTORY).deleteRecursively()
  }

  @Test
  fun constructor_writesDefaultSettings() {
    // Set up instantiates settings manager already

    assertThat(SETTINGS_FILE.exists()).isTrue()
    assertThat(SETTINGS_FILE.readText()).isNotEmpty()
  }

  @Test
  fun constructor_doesNotOverwriteExistingSettings() {
    SETTINGS_FILE.writeText("{}")

    // Re-instantiate
    manager =
      SettingsManagerImpl(
        gson = gson,
        logger = mockLogger,
        settingsFilePath = TESTING_DIRECTORY,
        lazyRelayFactory = mockLazyRelayFactory
      )

    // Verify what we wrote wasn't overwritten
    assertThat(SETTINGS_FILE.readText()).isEqualTo("{}")
  }

  @Test
  fun relay_invalidFile_writesExampleSettingsFile() {
    verify(mockLazyRelayFactory).create(relaySignalCaptor.capture(), relayCalculateCaptor.capture())

    SETTINGS_FILE.writeText("garbage { json")

    // Make
    try {
      relayCalculateCaptor.firstValue()
      assertWithMessage("Expected a JsonSyntaxException while decoding garbage json")
    } catch (e: JsonSyntaxException) {
      // Expected
    }

    assertThat(EXAMPLE_SETTINGS_FILE.exists()).isTrue()
  }

  @Test
  fun relay_signaledOnSettingsFileLastModified() {
    verify(mockLazyRelayFactory).create(relaySignalCaptor.capture(), relayCalculateCaptor.capture())

    assertThat(relaySignalCaptor.firstValue()).isEqualTo(SETTINGS_FILE.lastModified())
    assertThat(gson.toJson(relayCalculateCaptor.firstValue())).isEqualTo(SETTINGS_FILE.readText())

    SETTINGS_FILE.setLastModified(SETTINGS_FILE.lastModified() + 3000)

    assertThat(relaySignalCaptor.firstValue()).isEqualTo(SETTINGS_FILE.lastModified())
  }
}
