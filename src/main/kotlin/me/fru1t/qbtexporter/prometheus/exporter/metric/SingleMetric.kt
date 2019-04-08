package me.fru1t.qbtexporter.prometheus.exporter.metric

import me.fru1t.qbtexporter.prometheus.exporter.Metric
import me.fru1t.qbtexporter.prometheus.exporter.MetricType

/**
 * Implementation of [Metric] that represents a single-valued metric in prometheus.
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
  val value: Double,
  name: String,
  help: String,
  type: MetricType,
  isInteger: Boolean = false
) : Metric(name, help, type, isInteger) {
  override fun getAllInternalMetrics(): String = createInternalMetric(name, value)
}
