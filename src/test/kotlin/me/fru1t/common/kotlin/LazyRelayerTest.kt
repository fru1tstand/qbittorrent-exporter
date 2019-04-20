package me.fru1t.common.kotlin

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LazyRelayerTest {
  private companion object {
    private const val INITIAL_SIGNAL_VALUE = 0
    private const val INITIAL_CALCULATE_VALUE = 200
  }

  /**
   * A lazy relayer that returns the signal and calculation values based on fields that can be set
   * by test methods.
   */
  private class TestLazyRelayer : LazyRelayer<Int>() {
    var signal: Int = INITIAL_SIGNAL_VALUE
    var calculateValue: Int = INITIAL_CALCULATE_VALUE

    override fun signal(): Any? = signal
    override fun calculate(): Int = calculateValue
  }

  private lateinit var relayer: TestLazyRelayer

  @BeforeEach
  fun setUp() {
    relayer = TestLazyRelayer()
  }

  @Test
  fun relay() {
    val value: Int by relayer.relay()

    // Verify the relayer is doing its job by passing along the calculation value
    assertThat(value).isEqualTo(INITIAL_CALCULATE_VALUE)

    // And that even though we can manually update the value, the actual returned value won't update
    // as the signal hasn't changed
    relayer.calculateValue = INITIAL_CALCULATE_VALUE + 1
    assertThat(value).isEqualTo(INITIAL_CALCULATE_VALUE)

    // But when we update the signal, everything will update
    relayer.signal = INITIAL_SIGNAL_VALUE + 1
    assertThat(value).isEqualTo(INITIAL_CALCULATE_VALUE + 1)
  }

  @Test
  fun relay_withManipulation() {
    val value: String by relayer.relay { it.toString() }

    // Verify the relayer is doing its job by passing along the calculation value
    assertThat(value).isEqualTo(INITIAL_CALCULATE_VALUE.toString())

    // And that even though we can manually update the value, the actual returned value won't update
    // as the signal hasn't changed
    relayer.calculateValue = INITIAL_CALCULATE_VALUE + 1
    assertThat(value).isEqualTo(INITIAL_CALCULATE_VALUE.toString())

    // But when we update the signal, everything will update
    relayer.signal = INITIAL_SIGNAL_VALUE + 1
    assertThat(value).isEqualTo((INITIAL_CALCULATE_VALUE + 1).toString())
  }
}
