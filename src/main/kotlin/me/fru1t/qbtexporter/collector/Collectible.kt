package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.qbt.response.Maindata

/**
 * Qbt classes that are able to transform into a prometheus-readable metric available in this
 * exporter.
 */
interface Collectible {
  /**
   * Returns the metric name which must conform to
   * [prometheus naming convention](https://prometheus.io/docs/instrumenting/writing_exporters/#naming).
   */
  fun getName(): String

  /**
   * Returns the help text for this metric which will be rendered to prometheus via its `HELP`
   * comment in the exporter.
   */
  fun getMetricHelp(): String

  /** Returns the prometheus metric type. See [MetricType]. */
  fun getMetricType(): MetricType

  /**
   * Returns the key-value pair(s) portion of the metric given the [maindata] object.
   *
   * For example:
   * ```
   * my_metric 123
   * ```
   *
   * Or for labelled metrics:
   * ```
   * my_metric{foo="1",bar="a"} 123
   * my_metric{foo="2",bar="b"} 123
   * ```
   */
  fun getMetric(maindata: Maindata): String
}
