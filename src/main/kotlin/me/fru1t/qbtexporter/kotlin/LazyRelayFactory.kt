package me.fru1t.qbtexporter.kotlin

/** Creates [LazyRelay] instances. */
interface LazyRelayFactory {
  /**
   * Create a new [LazyRelay] that lazily updates its value when [signal] changes by calling
   * [calculate].
   */
  fun <T> create(signal: () -> Any?, calculate: () -> T): LazyRelay<T>
}
