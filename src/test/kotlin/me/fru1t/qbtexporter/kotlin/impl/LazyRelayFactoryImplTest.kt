package me.fru1t.qbtexporter.kotlin.impl

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class LazyRelayFactoryImplTest {
  @Test
  fun create() {
    val result = LazyRelayFactoryImpl().create({ 500 }) { 600 }

    assertThat(result.poll()).isEqualTo(600)
  }
}
