package me.fru1t.common.kotlin

/**
 * A class that will
 */
@Deprecated("Use project-specific implementation")
abstract class LazyRelayer<T> {
  private val value: LazyRelay<T> = LazyRelay(::signal, ::calculate)

  /**
   * Entry point for property delegation using this lazy relayer. One should instantiate a field
   * like so: `val foo = barInstance.relay()`.
   */
  fun relay(): LazyRelay<T> = value

  /**
   * Entry point for property delegation using this lazy relayer and manipulating its relayed value
   * to another value. One should instantiate a field like so:
   * `val foo = barInstance.relay() { it.baz }`.
   */
  fun <O> relay(manipulation: (T) -> O): LazyRelay<O> =
    LazyRelay(::signal) { manipulation(value.get()) }

  /**
   * A signal that determines when this lazy relayer should re-calculate its value. Note that due
   * to the lazy nature, a signal may change multiple times, but this call will only re-calculate
   * the value when it's accessed (ie. it works like a pull model instead of a push).
   */
  protected abstract fun signal(): Any?

  /** Produces the actual [T] value wanted. */
  protected abstract fun calculate(): T
}
