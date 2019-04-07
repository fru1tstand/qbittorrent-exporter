package me.fru1t.qbtexporter.logger.impl

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class ConsoleLoggerTest {
  private companion object {
    private const val TEST_MESSAGE = "this is a test message"
  }

  private lateinit var originalOut: PrintStream
  private lateinit var outContent: ByteArrayOutputStream

  @BeforeEach
  fun setUp() {
    originalOut = System.out
    outContent = ByteArrayOutputStream()
    System.setOut(PrintStream(outContent))
  }

  @AfterEach
  fun tearDown() {
    System.setOut(originalOut)
  }

  @Test
  fun log_level_off() {
    val logger = ConsoleLogger(ConsoleLogger.LOG_LEVEL_OFF)

    logger.d(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.i(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.w(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.e(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()
  }

  @Test
  fun log_level_error() {
    val logger = ConsoleLogger(ConsoleLogger.LOG_LEVEL_ERROR)

    logger.d(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.i(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.w(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.e(TEST_MESSAGE)
    assertAndConsumeMessage()
  }

  @Test
  fun log_level_warning() {
    val logger = ConsoleLogger(ConsoleLogger.LOG_LEVEL_WARNING)

    logger.d(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.i(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.w(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.e(TEST_MESSAGE)
    assertAndConsumeMessage()
  }

  @Test
  fun log_level_info() {
    val logger = ConsoleLogger(ConsoleLogger.LOG_LEVEL_INFO)

    logger.d(TEST_MESSAGE)
    assertThat(outContent.toString()).isEmpty()

    logger.i(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.w(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.e(TEST_MESSAGE)
    assertAndConsumeMessage()
  }

  @Test
  fun log_level_debug() {
    val logger = ConsoleLogger(ConsoleLogger.LOG_LEVEL_DEBUG)

    logger.d(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.i(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.w(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.e(TEST_MESSAGE)
    assertAndConsumeMessage()
  }

  @Test
  fun log_level_all() {
    val logger = ConsoleLogger(ConsoleLogger.LOG_LEVEL_ALL)

    logger.d(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.i(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.w(TEST_MESSAGE)
    assertAndConsumeMessage()

    logger.e(TEST_MESSAGE)
    assertAndConsumeMessage()
  }

  /** Asserts that the test message was `println`ed to the output, then resets the stream. */
  private fun assertAndConsumeMessage() {
    assertThat(outContent.toString()).isEqualTo(TEST_MESSAGE + "\n")
    outContent.reset()
  }
}
