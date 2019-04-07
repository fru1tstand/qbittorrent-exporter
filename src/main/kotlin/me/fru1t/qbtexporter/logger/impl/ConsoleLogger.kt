package me.fru1t.qbtexporter.logger.impl

import me.fru1t.qbtexporter.logger.Logger

/**
 * Implementation of [Logger] that outputs to the console if the requested log level is high enough.
 *
 * Ordering of log levels is as follows: `ALL > ERROR > WARNING > INFO > DEBUG > OFF`.
 *
 * @constructor logs to console when the log request is greater than or equal to the [logLevel].
 */
class ConsoleLogger(private val logLevel: Int) : Logger {
  companion object {
    const val LOG_LEVEL_ALL = 100
    const val LOG_LEVEL_DEBUG = 80
    const val LOG_LEVEL_INFO = 60
    const val LOG_LEVEL_WARNING = 40
    const val LOG_LEVEL_ERROR = 20
    const val LOG_LEVEL_OFF = -1
  }

  override fun d(message: String) {
    if (logLevel >= LOG_LEVEL_DEBUG) {
      println(message)
    }
  }

  override fun i(message: String) {
    if (logLevel >= LOG_LEVEL_INFO) {
      println(message)
    }
  }

  override fun w(message: String) {
    if (logLevel >= LOG_LEVEL_WARNING) {
      println(message)
    }
  }

  override fun e(message: String) {
    if (logLevel >= LOG_LEVEL_ERROR) {
      println(message)
    }
  }
}
