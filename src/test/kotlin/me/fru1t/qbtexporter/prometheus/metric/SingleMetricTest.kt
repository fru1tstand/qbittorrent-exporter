package me.fru1t.qbtexporter.prometheus.metric

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.prometheus.MetricType
import org.junit.jupiter.api.Test

internal class SingleMetricTest {
  private companion object {
    private const val TEST_NAME = "metric_name"
    private const val TEST_HELP = "this is a help"
    private val TEST_METRIC_TYPE = MetricType.HISTOGRAM
  }

  @Test
  fun testToString() {
    val singleMetric =
      SingleMetric(
        value = 31415.92,
        name = TEST_NAME,
        help = TEST_HELP,
        type = TEST_METRIC_TYPE
      )

    assertThat(singleMetric.toString()).isEqualTo(
      "# HELP $TEST_NAME $TEST_HELP\n" +
          "# TYPE $TEST_NAME ${TEST_METRIC_TYPE.name.toLowerCase()}\n" +
          "$TEST_NAME 3.141592e+04"
    )
  }
}
