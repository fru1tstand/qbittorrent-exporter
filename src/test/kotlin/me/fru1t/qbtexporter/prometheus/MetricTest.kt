package me.fru1t.qbtexporter.prometheus

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.jupiter.api.Test

internal class MetricTest {
  private companion object {
    private const val TEST_METRIC_NAME = "this_is_a_test_name"
    private const val TEST_METRIC_HELP = "Example test for help."
    private val TEST_METRIC_TYPE = MetricType.COUNTER
  }

  @Test
  fun toString_byte() {
    val value: Byte = 3
    assertToString(value, "3")
  }

  @Test
  fun toString_short() {
    val value: Short = 7
    assertToString(value, "7")
  }

  @Test
  fun toString_int() {
    val value: Int? = 400
    assertToString(value, "400")
  }

  @Test
  fun toString_long() {
    val value: Long = 173
    assertToString(value, "173")
  }

  @Test
  fun toString_float() {
    val value: Float? = 3.14F
    assertToString(value, "3.14")
  }

  @Test
  fun toString_double() {
    val value: Double? = 3.1415
    assertToString(value, "3.1415")
  }

  @Test
  fun invalidName_throwsIllegalArgumentException() {
    try {
      object : Metric(name = "invalid name", help = "", type = TEST_METRIC_TYPE) {
        override fun getAllInternalMetrics(): String = ""
      }
      assertWithMessage("Expected an IllegalArgumentException about an invalid name")
    } catch (e: IllegalArgumentException) {
      // Expected
      assertThat(e).hasMessageThat().contains("invalid name")
    }
  }

  @Test
  fun emptyInternalMetrics_producesNothing() {
    val metric = object : Metric(name = "test", help = "test help", type = TEST_METRIC_TYPE) {
      override fun getAllInternalMetrics(): String = ""
    }

    assertThat(metric.toString()).isEmpty()
  }

  /** Creates a new metric and tests its output against the expected metric value. */
  private fun assertToString(value: Number?, expectedValue: String) {
    val metric =
      object : Metric(
        name = TEST_METRIC_NAME,
        help = TEST_METRIC_HELP,
        type = TEST_METRIC_TYPE
      ) {
        override fun getAllInternalMetrics(): String =
          createInternalMetric(TEST_METRIC_NAME, value)
      }

    assertThat(metric.toString()).isEqualTo(
      "# HELP $TEST_METRIC_NAME $TEST_METRIC_HELP\n" +
          "# TYPE $TEST_METRIC_NAME ${TEST_METRIC_TYPE.name.toLowerCase()}\n" +
          "$TEST_METRIC_NAME $expectedValue"
    )
  }
}
