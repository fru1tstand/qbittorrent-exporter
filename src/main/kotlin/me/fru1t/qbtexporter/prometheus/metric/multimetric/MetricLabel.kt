package me.fru1t.qbtexporter.prometheus.metric.multimetric

import java.lang.IllegalArgumentException
import java.util.regex.Pattern

/**
 * The labels within a multi metric.
 *
 * MetricLabels are what appear on multi metrics like the following:
 *
 * ```
 * example_metric{foo="foo value",bar="bar value"}
 * ```
 *
 * Where `foo` and `bar` are both labels.
 */
class MetricLabel private constructor(private val labels: Map<String, String>) {
  companion object {
    private const val LABEL_TEMPLATE = "%s=\"%s\""
    private const val LABEL_SEPARATOR = ","

    private val KEY_FAIL_REGEX = Pattern.compile("[^a-zA-Z0-9_]")
    private val VALUE_FAIL_REGEX = Pattern.compile("[\"]")
  }

  /** Builds [MetricLabel]s. */
  class Builder {
    private val labels = HashMap<String, String>()

    /** Adds a single label-value pair to this builder. */
    fun addLabel(key: String, value: String): Builder {
      if (KEY_FAIL_REGEX.matcher(key).find()) {
        throw IllegalArgumentException("Invalid key for label: $key")
      }
      if (VALUE_FAIL_REGEX.matcher(value).find()) {
        throw IllegalArgumentException("Invalid value for label: $value")
      }
      labels[key] = value
      return this
    }

    /** Constructs a new [MetricLabel] from the labels added to this builder. */
    fun build(): MetricLabel = MetricLabel(labels)
  }

  /**
   * Formats this label to its exporter string of `key="value"` delimited by commas if there are
   * multiple labels.
   */
  override fun toString(): String {
    return labels.toList().joinToString(separator = LABEL_SEPARATOR) {
      LABEL_TEMPLATE.format(it.first, it.second)
    }
  }
}
