package me.fru1t.qbtexporter.prometheus.metric.multimetric

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class MetricLabelTest {
  @Nested
  internal inner class BuilderTest {
    @Test
    fun addLabel_throwsIllegalArgumentException_whenInvalidKey() {
      try {
        MetricLabel.Builder().addLabel("invalid key", "valid value")
        assertWithMessage("Expected #put to throw IllegalArgumentException.").fail()
      } catch (e: IllegalArgumentException) {
        assertThat(e).hasMessageThat().contains("Invalid key")
      }
    }

    @Test
    fun addLabel_throwsIllegalArgumentException_whenInvalidValue() {
      try {
        MetricLabel.Builder().addLabel("valid_key", "invalid \" value")
        assertWithMessage("Expected #put to throw IllegalArgumentException.").fail()
      } catch (e: IllegalArgumentException) {
        assertThat(e).hasMessageThat().contains("Invalid value")
      }
    }

    @Test
    fun build() {
      val result = MetricLabel.Builder().build()

      assertThat(result).isInstanceOf(MetricLabel::class.java)
    }
  }

  @Test
  fun testToString() {
    val label =
      MetricLabel.Builder()
        .addLabel("valid_key", "valid_value")
        .addLabel("valid_key2", "valid_value2")
        .build()

    val result = label.toString()

    // Ultimately, we don't care what the output order is, as long as they're both present
    val validOutput1 = "valid_key=\"valid_value\",valid_key2=\"valid_value2\""
    val validOutput2 = "valid_key2=\"valid_value2\",valid_key=\"valid_value\""
    if (result != validOutput1 && result != validOutput2) {
      assertWithMessage(
        "Output did not match expected. \nGot $result\nExpected $validOutput1\nOr $validOutput2")
        .fail()
    }
  }
}
