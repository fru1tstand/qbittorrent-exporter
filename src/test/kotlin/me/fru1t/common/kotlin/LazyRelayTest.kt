package me.fru1t.common.kotlin

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class LazyRelayTest {
  /** An implementation of a [LazyRelayer] that returns the values within its fields. */
  private class TestRelayer : LazyRelayer<Int> {
    var signalValue: Int = 0
    var calculateValue: Int = 0

    override fun signal(): Any? {
      return signalValue
    }

    override fun calculate(): Int {
      return calculateValue
    }
  }

  @Test
  fun doesNotUpdateUntilFirstAccess() {
    var calculatedValue = 0

    val subject: Int by lazyRelay({ 1 }) { calculatedValue++ }

    assertThat(calculatedValue).isEqualTo(0)
  }

  @Test
  fun nonUpdatingDelegate_doesNotCallProducer() {
    var calculatedValue = 0

    val subject: Int by lazyRelay({ 1 }) { calculatedValue++ }

    // Initial lazy load
    assertThat(subject).isEqualTo(0)

    // Subsequent calls do not update subject
    assertThat(subject).isEqualTo(0)
    assertThat(subject).isEqualTo(0)
    assertThat(subject).isEqualTo(0)
  }

  @Test
  fun updatingDelegate_callsProducer() {
    var calculatedValue = 0
    var signalValue = 0

    val subject: Int by lazyRelay({ signalValue }) { calculatedValue++ }

    assertThat(subject).isEqualTo(0)

    signalValue++
    assertThat(subject).isEqualTo(1)
  }

  @Test
  fun relayerExtensionMethod() {
    val relayer = TestRelayer()

    val subject: Int by relayer.lazyRelay()

    assertThat(subject).isEqualTo(relayer.calculateValue)
  }
}
