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
  val type: MetricType
) {
  protected companion object {
    private const val HELP_TEMPLATE = "# HELP %s %s"
    private const val TYPE_TEMPLATE = "# TYPE %s %s"

    private val NAMING_REGEX = Pattern.compile("[a-zA-Z0-9:_]+")!!

    /**
     * Returns a properly formatted internal metric line with the given [name] and [value], coercing
     * [value] to 0 if passed `null`.
     */
    fun createInternalMetric(name: String, value: Number?): String = "$name ${value ?: 0}"
  }

  /** Pre-computed help and type comment headers for prometheus export. */
  private val metricHeader = "" +
      HELP_TEMPLATE.format(name, help) + "\n" +
      TYPE_TEMPLATE.format(name, type.name.toLowerCase()) + "\n"

  init {
    // Validate name
    if (!isValidIdentifier(name)) {
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

  /**
   * Returns the full metric string prometheus looks for including help and metric type comments.
   *
   * This method returns an empty string if no internal metrics are generated.
   */
  override fun toString(): String =
    getAllInternalMetrics().let { if (it.isBlank()) "" else metricHeader + it }

  /** Returns whether or not [name] is a valid prometheus metric name or label. */
  protected fun isValidIdentifier(name: String) = NAMING_REGEX.matcher(name).matches()
}
