package me.fru1t.qbtexporter.prometheus.metric

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType

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
  isWholeNumber: Boolean = false
) : Metric(name, help, type, isWholeNumber) {
  override fun getAllInternalMetrics(): String = createInternalMetric(name, value)
}
