package me.fru1t.qbtexporter.prometheus.metric

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType

/**
 * Implementation of [Metric] that represents multiple-valued metrics, identified by labels, in
 * prometheus.
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
  val metrics: Map<Map<String, String>, Double>,
  name: String,
  help: String,
  type: MetricType,
  isInteger: Boolean = false
) : Metric(name, help, type, isInteger) {
  private companion object {
    private const val NAME_WITH_LABELS_TEMPLATE = "%s{%s}"
    private const val LABEL_TEMPLATE = "%s=\"%s\""
    private const val LABEL_SEPARATOR = ","
    private const val METRIC_SEPARATOR = "\n"
  }

  init {
    // Validate all labels
    metrics.keys.forEach { labels ->
      labels.forEach { labelName, labelValue ->
        if (!isValidName(labelName)) {
          throw IllegalArgumentException(
            "Illegal metric label '$labelName' (with value '$labelValue') in metric '$name'"
          )
        }

        if (labelValue.contains("\"")) {
          throw IllegalArgumentException(
            "Illegal metric label value '$labelValue' for label '$labelName' in metric '$name'"
          )
        }
      }
    }
  }

  override fun getAllInternalMetrics(): String =
    metrics.toList().joinToString(separator = METRIC_SEPARATOR) { metricPair ->
      createInternalMetric(
        createNameFromLabels(metricPair.first),
        metricPair.second
      )
    }

  /** Creates the name of a single metric from the given labels list. */
  private fun createNameFromLabels(labels: Map<String, String>): String {
    val collapsedLabels = labels.toList().joinToString(separator = LABEL_SEPARATOR) { labelPair ->
      LABEL_TEMPLATE.format(
        labelPair.first,
        labelPair.second
      )
    }
    return NAME_WITH_LABELS_TEMPLATE.format(name, collapsedLabels)
  }
}
