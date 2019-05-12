package me.fru1t.qbtexporter.prometheus.metric

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.multimetric.MetricLabel
import org.junit.jupiter.api.Test

internal class MultiMetricTest {
  private companion object {
    private const val TEST_METRIC_NAME = "this_is_a_test_name"
    private const val TEST_METRIC_HELP = "Example test for help."
    private val TEST_METRIC_TYPE = MetricType.COUNTER
  }

  @Test
  fun testToString() {
    val metricLabel1 =
      MetricLabel.Builder().addLabel("foo", "fooValue1").addLabel("bar", "barValue1").build()
    val metricLabel2 =
      MetricLabel.Builder().addLabel("foo", "fooValue2").addLabel("bar", "barValue2").build()
    val metricLabel3 = MetricLabel.Builder().addLabel("baz", "bazValue3").build()
    val metrics = mapOf<MetricLabel, Number>(
      Pair(metricLabel1, 1.0),
      Pair(metricLabel2, 2),
      Pair(metricLabel3, 3.0)
    )

    val multiMetric =
      MultiMetric(
        metrics = metrics,
        name = TEST_METRIC_NAME,
        help = TEST_METRIC_HELP,
        type = TEST_METRIC_TYPE
      )

    assertThat(multiMetric.toString()).isEqualTo(
      "# HELP $TEST_METRIC_NAME $TEST_METRIC_HELP\n" +
          "# TYPE $TEST_METRIC_NAME ${TEST_METRIC_TYPE.name.toLowerCase()}\n" +
          "$TEST_METRIC_NAME{$metricLabel1} 1.0\n" +
          "$TEST_METRIC_NAME{$metricLabel2} 2\n" +
          "$TEST_METRIC_NAME{$metricLabel3} 3.0"
    )
  }

  @Test
  fun toString_withNoMetrics_producesNothing() {
    val multiMetric =
      MultiMetric(
        metrics = mapOf(),
        name = TEST_METRIC_NAME,
        help = TEST_METRIC_HELP,
        type = TEST_METRIC_TYPE
      )

    assertThat(multiMetric.toString()).isEmpty()
  }
}
