package me.fru1t.qbtexporter.logger

/** Exposes methods for logging to an output. */
interface Logger {
  /**
   * Logs a debug/verbose message. These are usually for developmental purposes only and would not
   * be visible to an end-user running the software.
   */
  fun d(message: String)

  /** Logs an informational message. These are usually for debugging purposes for an end-user. */
  fun i(message: String)

  /** Logs a warning. These are usually for recoverable errors. */
  fun w(message: String)

  /** Logs an error. These are usually for irrecoverable errors. */
  fun e(message: String)
}
