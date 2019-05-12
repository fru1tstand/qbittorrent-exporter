package me.fru1t.qbtexporter.prometheus.metric

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.prometheus.metric.multimetric.MetricLabel

/**
 * Implementation of [Metric] that represents multiple-valued metrics, identified by labels, in
 * prometheus. Note that this metric is re-usable such that a collector may create an instance of
 * it, and set its value multiple times.
 *
 * For example
 * ```
 * # HELP example_metric This is an example metric.
 * # TYPE example_metric counter
 * example_metric{foo="1",bar="a"} 111
 * example_metric{foo="2",bar="b"} 222
 * ```
 *
 * @constructor creates a metric that may contain multiple output [metrics]. The key of [metrics]
 * represents the [labels](https://prometheus.io/docs/instrumenting/writing_exporters/#labels) in
 * key-value pairs, and the value of [metrics] represents the value of that metric. See [Metric] for
 * additional parameter details.
 */
class MultiMetric(
  var metrics: Map<MetricLabel, Number?>,
  name: String,
  help: String,
  type: MetricType
) : Metric(name, help, type) {
  private companion object {
    private const val NAME_WITH_LABELS_TEMPLATE = "%s{%s}"
    private const val METRIC_SEPARATOR = "\n"
  }

  override fun getAllInternalMetrics(): String =
    metrics.toList().joinToString(separator = METRIC_SEPARATOR) { metricLabelAndValue ->
      createInternalMetric(
        NAME_WITH_LABELS_TEMPLATE.format(name, metricLabelAndValue.first.toString()),
        metricLabelAndValue.second
      )
    }
}
