package me.fru1t.qbtexporter.cli

/** Processes and takes action on CLI flags. */
interface Flags {
  /**
   * Handles any startup flags that were passed through the CLI. Returns whether flags were present.
   * If flags were present, the experter server should not be started.
   */
  fun handle(): Boolean
}
