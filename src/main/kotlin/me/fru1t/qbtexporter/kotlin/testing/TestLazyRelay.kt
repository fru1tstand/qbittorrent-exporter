package me.fru1t.qbtexporter.kotlin.testing

import me.fru1t.qbtexporter.kotlin.LazyRelay
import org.jetbrains.annotations.TestOnly

/** A [LazyRelay] implementation that always returns a single value for testing purposes only. */
internal class TestLazyRelay<T> private constructor(val value: T) : LazyRelay<T> {
  companion object {
    /** Creates a new [LazyRelay] that only returns [value]. */
    @TestOnly
    fun <T> of(value: T): LazyRelay<T> = TestLazyRelay(value)
  }

  override fun poll(): T = value
}
