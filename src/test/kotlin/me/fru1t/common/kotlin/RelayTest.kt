package me.fru1t.common.kotlin

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class RelayTest {
  /** An implementation of a [Relayer] that returns the values within its fields. */
  private class TestRelayer : Relayer<Int> {
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

    val subject: Int by relay({ 1 }) { calculatedValue++ }

    assertThat(calculatedValue).isEqualTo(0)
  }

  @Test
  fun nonUpdatingDelegate_doesNotCallProducer() {
    var calculatedValue = 0

    val subject: Int by relay({ 1 }) { calculatedValue++ }

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

    val subject: Int by relay({ signalValue }) { calculatedValue++ }

    assertThat(subject).isEqualTo(0)

    signalValue++
    assertThat(subject).isEqualTo(1)
  }

  @Test
  fun relayerExtensionMethod() {
    val relayer = TestRelayer()

    val subject: Int by relayer.relay()

    assertThat(subject).isEqualTo(relayer.calculateValue)
  }
}
