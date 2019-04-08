package me.fru1t.qbtexporter.prometheus.exporter

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
  fun toString_double() {
    val metric =
      object : Metric(
        name = TEST_METRIC_NAME,
        help = TEST_METRIC_HELP,
        type = TEST_METRIC_TYPE,
        isInteger = false
      ) {
        override fun getAllInternalMetrics(): String =
          createInternalMetric(TEST_METRIC_NAME, 31415.92)
      }

    assertThat(metric.toString()).isEqualTo(
      "# HELP $TEST_METRIC_NAME $TEST_METRIC_HELP\n" +
          "# TYPE $TEST_METRIC_NAME ${TEST_METRIC_TYPE.name.toLowerCase()}\n" +
          "$TEST_METRIC_NAME 3.141592e+04"
    )
  }

  @Test
  fun toString_integer() {
    val metric =
      object : Metric(
        name = TEST_METRIC_NAME,
        help = TEST_METRIC_HELP,
        type = TEST_METRIC_TYPE,
        isInteger = true
      ) {
        override fun getAllInternalMetrics(): String =
          createInternalMetric(TEST_METRIC_NAME, 3.14)
      }

    assertThat(metric.toString()).isEqualTo(
      "# HELP $TEST_METRIC_NAME $TEST_METRIC_HELP\n" +
          "# TYPE $TEST_METRIC_NAME ${TEST_METRIC_TYPE.name.toLowerCase()}\n" +
          "$TEST_METRIC_NAME 3"
    )
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
}
