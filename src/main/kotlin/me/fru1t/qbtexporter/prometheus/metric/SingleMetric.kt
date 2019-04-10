package me.fru1t.qbtexporter.prometheus.metric

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType

/**
 * Implementation of [Metric] that represents a single-valued metric in prometheus. Note that this
 * metric is re-usable such that a collector may create an instance of it, and set its value
 * multiple times.
 *
 * For example
 * ```
 * # HELP example_metric This is an example metric.
 * # TYPE example_metric counter
 * example_metric 123
 * ```
 *
 * @constructor creates a metric that contains a single output with a [value]. See [Metric] for
 * additional parameter details.
 */
class SingleMetric(
  var value: Number?,
  name: String,
  help: String,
  type: MetricType
) : Metric(name, help, type) {
  override fun getAllInternalMetrics(): String = createInternalMetric(name, value)
}
