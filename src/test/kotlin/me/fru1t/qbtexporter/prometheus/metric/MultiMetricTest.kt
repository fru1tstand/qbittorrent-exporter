package me.fru1t.qbtexporter.prometheus.metric

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import me.fru1t.qbtexporter.prometheus.MetricType
import org.junit.jupiter.api.Test

internal class MultiMetricTest {
  private companion object {
    private const val TEST_METRIC_NAME = "this_is_a_test_name"
    private const val TEST_METRIC_HELP = "Example test for help."
    private val TEST_METRIC_TYPE = MetricType.COUNTER
  }

  @Test
  fun testToString() {
    val metrics = mapOf<Map<String, String>, Number>(
      Pair(
        mapOf(Pair("foo", "fooValue1"), Pair("bar", "barValue1")),
        1.0
      ),
      Pair(
        mapOf(Pair("foo", "fooValue2"), Pair("bar", "barValue2")),
        2
      ),
      Pair(
        mapOf(Pair("baz", "bazValue3")),
        3.0
      )
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
          "$TEST_METRIC_NAME{foo=\"fooValue1\",bar=\"barValue1\"} 1.0\n" +
          "$TEST_METRIC_NAME{foo=\"fooValue2\",bar=\"barValue2\"} 2\n" +
          "$TEST_METRIC_NAME{baz=\"bazValue3\"} 3.0"
    )
  }

  @Test
  fun invalidLabelName_throwsIllegalArgumentException() {
    val metrics = mapOf(Pair(mapOf(Pair("invalid name", "value")), 1.0))
    try {
      MultiMetric(
        metrics = metrics,
        name = TEST_METRIC_NAME,
        help = TEST_METRIC_HELP,
        type = TEST_METRIC_TYPE
      )
      assertWithMessage("Expected IllegalArgumentException about invalid label name.")
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().contains("Illegal metric label 'invalid name'")
    }
  }

  @Test
  fun invalidLabelValue_throwsIllegalArgumentException() {
    val metrics = mapOf(Pair(mapOf(Pair("name", "invalid value \"")), 1.0))
    try {
      MultiMetric(
        metrics = metrics,
        name = TEST_METRIC_NAME,
        help = TEST_METRIC_HELP,
        type = TEST_METRIC_TYPE
      )
      assertWithMessage("Expected IllegalArgumentException about invalid label value.")
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().contains("Illegal metric label value 'invalid value \"'")
    }
  }
}
