package me.fru1t.qbtexporter.cli.impl

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import me.fru1t.qbtexporter.logger.Logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class FlagsImplTest {
  @Mock private lateinit var logger: Logger
  private lateinit var stringCaptor: KArgumentCaptor<String>

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    stringCaptor = argumentCaptor()
  }

  @Test
  fun handle_noArgs_returnsTrue() {
    val flags = createFlags(arrayOf())

    assertThat(flags.handle()).isFalse()
    verify(logger, never()).w(any())
  }

  @Test
  fun handle_multipleArgs_returnsTrueAndErrors() {
    val flags = createFlags(arrayOf("--help", "--other"))

    assertThat(flags.handle()).isTrue()
    verify(logger).w(stringCaptor.capture())
    assertThat(stringCaptor.firstValue).contains("Only a single flag can be passed in")
  }

  @Test
  fun handle_help() = verifyHandle("--help") {
    assertThat(it).contains("Usage: exporter.jar")
  }

  @Test
  fun handle_h() = verifyHandle("-h") {
    assertThat(it).contains("Usage: exporter.jar")
  }

  @Test
  fun handle_collectors() = verifyHandle("--collectors") {
    assertThat(it).contains("Collectors (as they appear in settings)")
  }

  @Test
  fun handle_c() = verifyHandle("-c") {
    assertThat(it).contains("Collectors (as they appear in settings)")
  }

  @Test
  fun handle_settings() = verifyHandle("--settings") {
    assertThat(it).contains("Settings")
  }

  @Test
  fun handle_s() = verifyHandle("-s") {
    assertThat(it).contains("Settings")
  }

  /**
   * Verifies that by passing [flag] into the cli, `handle` will return `true`, and the information
   * output of the logger will conform to [outputValidation].
   */
  private fun verifyHandle(flag: String, outputValidation: ((String) -> Unit)? = null) {
    val flags = createFlags(arrayOf(flag))

    assertThat(flags.handle()).isTrue()
    verify(logger).i(stringCaptor.capture())
    outputValidation?.let { it(stringCaptor.firstValue) }
  }

  /** Creates a [FlagsImpl] with a mock logger and the given [args]. */
  private fun createFlags(args: Array<String>) = FlagsImpl(logger = logger, args = args)
}
