package me.fru1t.common.kotlin

import kotlin.reflect.KProperty

/**
 * Method entry point for property delegation `by lazyRelay` on objects that don't extend
 * [LazyRelayer]. See [LazyRelay].
 */
fun <T> lazyRelay(signal: () -> Any?, calculate: () -> T): LazyRelay<T> =
  LazyRelay(signal = signal, calculate = calculate)

/**
 * A field that acts like a lazy loaded field, but also re-[calculate]s its value when it detects
 * that a [signal] value has changed. Detection is done upon field accessing, so while a [signal]
 * may change multiple times, [calculate] will only be run when the field is accessed.
 *
 * This is useful for scenarios where a heavy operation is required to calculate the value for a
 * field, but doesn't need re-calculation for every single access. Thus, we [signal] the need to
 * re-[calculate].

 * Note the terminology comes from electrical relays where a "low power" signal can control a "high
 * power" output.
 */
class LazyRelay<T>(private val signal: () -> Any?, private val calculate: () -> T) {
  private companion object {
    private object UninitializedValue
  }

  @Volatile private var actualValue: Any? = UninitializedValue
  @Volatile private var signalValue: Any? = UninitializedValue

  /** Returns the value of this relay. */
  fun get(): T {
    val newSignalValue = signal()
    if (actualValue == UninitializedValue || signalValue != newSignalValue) {
      actualValue = calculate()
      signalValue = newSignalValue
    }

    @Suppress("UNCHECKED_CAST")
    return actualValue as T
  }

  /** Allows instances of this object to be used in property delegation (ie. `by lazyRelay`). */
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
}
