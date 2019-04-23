package me.fru1t.qbtexporter.kotlin.impl

import me.fru1t.qbtexporter.kotlin.LazyRelay

/** Implementation of [LazyRelay]. */
class LazyRelayImpl<T>(
  private val signal: () -> Any?,
  private val calculate: () -> T
) : LazyRelay<T> {
  private companion object {
    private object UninitializedValue
  }

  @Volatile private var actualValue: Any? = UninitializedValue
  @Volatile private var signalValue: Any? = UninitializedValue

  override fun poll(): T {
    val newSignalValue = signal()
    if (actualValue == UninitializedValue || signalValue != newSignalValue) {
      actualValue = calculate()
      signalValue = newSignalValue
    }

    @Suppress("UNCHECKED_CAST")
    return actualValue as T
  }
}
