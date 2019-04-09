package me.fru1t.qbtexporter.prometheus

import java.util.regex.Pattern

/**
 * Produces prometheus-consumable output.
 *
 * @constructor creates a metric with a [name] that must conform to
 * [prometheus documentation],
 * with [help] and a given [type].
 */
abstract class Metric(
  val name: String,
  val help: String,
  val type: MetricType,
  val isWholeNumber: Boolean = false
) {
  private companion object {
    private const val HELP_TEMPLATE = "# HELP %s %s"
    private const val TYPE_TEMPLATE = "# TYPE %s %s"
    private const val METRIC_OUTPUT_DOUBLE_TEMPLATE = "%s %e"
    private const val METRIC_OUTPUT_INTEGER_TEMPLATE = "%s %d"

    private val NAMING_REGEX = Pattern.compile("[a-zA-Z0-9:_]+")!!
  }

  init {
    // Validate name
    if (!isValidName(name)) {
      throw IllegalArgumentException(
        "Illegal metric name '$name'. Must conform to regex ${NAMING_REGEX.pattern()}"
      )
    }
  }

  /**
   * Returns all name-value pairs this metric represents (ie. a metric's raw output that doesn't
   * include the help and type strings).
   */
  protected abstract fun getAllInternalMetrics(): String

  override fun toString(): String = "" +
      HELP_TEMPLATE.format(name, help) + "\n" +
      TYPE_TEMPLATE.format(name, type.name.toLowerCase()) + "\n" +
      getAllInternalMetrics()

  /** Returns whether or not [name] is a valid prometheus metric name or label. */
  protected fun isValidName(name: String) = NAMING_REGEX.matcher(name).matches()

  /** Returns a properly formatted internal metric line with the given [name] and [value]. */
  protected fun createInternalMetric(name: String, value: Double) =
    if (isWholeNumber) METRIC_OUTPUT_INTEGER_TEMPLATE.format(name, value.toInt())
    else METRIC_OUTPUT_DOUBLE_TEMPLATE.format(name, value)
}
