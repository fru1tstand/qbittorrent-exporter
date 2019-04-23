package me.fru1t.qbtexporter.kotlin.testing

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class TestLazyRelayTest {
  @Test
  fun of() {
    val value = "a value"

    val result = TestLazyRelay.of(value)

    assertThat(result).isInstanceOf(TestLazyRelay::class.java)
  }

  @Test
  fun poll() {
    var value = 1

    val result = TestLazyRelay.of(value)

    assertThat(result.poll()).isEqualTo(1)

    @Suppress("UNUSED_VALUE")
    value = 2
    assertThat(result.poll()).isEqualTo(1)
  }
}
