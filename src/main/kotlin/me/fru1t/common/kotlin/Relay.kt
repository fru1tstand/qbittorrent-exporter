package me.fru1t.common.kotlin

import kotlin.reflect.KProperty

/**
 * Method entry point for property delegation `by relay` on objects that don't extend [Relayer]. See
 * [Relay].
 */
fun <T> relay(signal: () -> Any?, calculate: () -> T): Relay<T> =
  Relay(signal = signal, calculate = calculate)

/** Method entry point for property delegation `by Relayer.relay`. See [Relay]. */
fun <T> Relayer<T>.relay(): Relay<T> = relay(signal = this::signal, calculate = this::calculate)

/**
 * A field whose value is [calculate]ed when a [signal] value is changed. This field will initially
 * act like a lazy loaded field.
 *
 * This is useful for scenarios where a heavy operation is required to calculate the value for a
 * field, but doesn't need re-calculation for every single access. Thus, we [signal] the need to
 * re-[calculate].
 *
 * Warning: while [signal] may change output at any given time, [calculate] is only called on
 * property access. To put another way, these methods are used as a poll implementation and not
 * push.
 *
 * Note the terminology comes from electrical relays where a "low power" signal can control a "high
 * power" output.
 */
class Relay<T>(private val signal: () -> Any?, private val calculate: () -> T) {
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

  /** Allows instances of this object to be used in property delegation (ie. `by relay`). */
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
}

/**
 * Denotes that a class will [calculate] new [T] values when a [signal] output changes. See [Relay].
 */
interface Relayer<T> {
  /**
   * Produces a value that, if changed, will cause the next access to the [Relay] to be
   * re-[calculate]ed.
   */
  fun signal(): Any?

  /** Produces the real output value for the [Relay]. */
  fun calculate(): T
}
