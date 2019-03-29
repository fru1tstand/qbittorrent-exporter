package me.fru1t.qbtexporter

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class QbtExporterTest {
  @Test
  fun init() {
    assertThat(QbtExporter()).isNotNull()
    // Does not crash
  }
}
