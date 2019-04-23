package me.fru1t.qbtexporter.kotlin.impl

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class LazyRelayImplTest {
  @Test
  fun doesNotUpdateUntilFirstAccess() {
    var calculatedValue = 0

    @Suppress("UNUSED_VARIABLE")
    val relay = LazyRelayImpl({ 1 }) { calculatedValue++ }

    assertThat(calculatedValue).isEqualTo(0)
  }

  @Test
  fun get_doesNotCallCalulate_whenSignalDoesNotUpdate() {
    var returnValue = 40

    val subject = LazyRelayImpl({ 1 }) { returnValue }

    // Initial lazy load
    assertThat(subject.poll()).isEqualTo(40)

    // Subsequent calls do not update subject even though the calculate method will return a
    // different value
    returnValue = 1000
    assertThat(subject.poll()).isEqualTo(40)
    assertThat(subject.poll()).isEqualTo(40)
    assertThat(subject.poll()).isEqualTo(40)
  }

  @Test
  fun updatingDelegate_callsProducer() {
    var returnValue = 0
    var signalValue = 0

    val subject = LazyRelayImpl({ signalValue }) { returnValue }

    assertThat(subject.poll()).isEqualTo(0)

    signalValue++
    returnValue++
    assertThat(subject.poll()).isEqualTo(1)
  }
}
