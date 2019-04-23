package me.fru1t.qbtexporter.kotlin

/**
 * A container object that initially acts like a lazy loader, but also re-calculates the value if a
 * signal input has been changed. Note that while the signal input may change multiple times, the
 * value is only re-calculated when this [LazyRelay] is [poll]ed.
 *
 * This is useful for scenarios where a value is computed via a heavy call; however, does not need
 * to be re-calculated every single time it's needed. In this case, a proxy value that's much
 * more lightweight can be used to determine if the heavy call is required. A concrete example of
 * this is a text editing program that wants to detect if a file that's open has been modified on
 * disk. The signal could be the date the file on disk was last modified, and the calculation is
 * the text within the file.
 *
 * The naming for this comes from electrical relays where a low power signal controls a higher power
 * output.
 */
interface LazyRelay<T> {
  /** Retrieves the current value of this relay, re-calculating its value if necessary. */
  fun poll(): T
}
