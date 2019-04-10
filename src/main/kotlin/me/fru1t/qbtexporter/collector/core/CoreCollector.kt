package me.fru1t.qbtexporter.collector.core

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.prometheus.MetricType
import me.fru1t.qbtexporter.collector.Collectible
import me.fru1t.qbtexporter.prometheus.metric.SingleMetric
import me.fru1t.qbtexporter.qbt.response.Maindata

/** A collector that directly translate the `maindata` qBittorrent api call to metrics. */
enum class CoreCollector(
  val category: CoreCollectorCategory,
  val type: MetricType,
  val help: String,
  val collect: (Maindata) -> Metric
) : Collectible {
  ALL_TIME_DOWNLOAD_BYTES(
    CoreCollectorCategory.SERVER,
    MetricType.COUNTER,
    "The total number of downloaded bytes from all torrents (including deleted ones) " +
        "across all sessions.",
    { maindata ->
      singleMetricOf(
        coreCollector = ALL_TIME_DOWNLOAD_BYTES,
        value = maindata.serverState?.allTimeDownloadedBytes
      )
    }
  ),

  ALL_TIME_UPLOAD_BYTES(
    CoreCollectorCategory.SERVER,
    MetricType.COUNTER,
    "The total number of uploaded bytes from all torrents (including deleted ones) " +
        "across all sessions.",
    { maindata ->
      singleMetricOf(
        coreCollector = ALL_TIME_UPLOAD_BYTES,
        value = maindata.serverState?.allTimeUploadedBytes
      )
    }
  )

  ;

  private companion object {
    /** [singleMetricOf] Long values. */
    private fun singleMetricOf(coreCollector: CoreCollector, value: Long?) =
      singleMetricOf(
        coreCollector = coreCollector,
        isWholeNumber = true,
        value = value?.toDouble()
      )

    /** [singleMetricOf] Int values. */
    private fun singleMetricOf(coreCollector: CoreCollector, value: Int?) =
      singleMetricOf(
        coreCollector = coreCollector,
        isWholeNumber = true,
        value = value?.toDouble()
      )

    /** [singleMetricOf] Boolean values coercing true to `1.0` and false to `0.0`. */
    private fun singleMetricOf(coreCollector: CoreCollector, isInteger: Boolean, value: Boolean?) =
      singleMetricOf(
        coreCollector,
        isInteger,
        if (value == true) 1.0 else 0.0
      )

    /**
     * Creates a [SingleMetric] of [coreCollector] and its calculated [value]. Automatically coerces
     * a `null` [value] to `0.0`.
     */
    private fun singleMetricOf(
      coreCollector: CoreCollector,
      isWholeNumber: Boolean,
      value: Double?
    ): SingleMetric =
      SingleMetric(
        value = value ?: 0.0,
        name = coreCollector.name.toLowerCase(),
        help = coreCollector.help,
        type = coreCollector.type,
        isWholeNumber = isWholeNumber
      )
  }

  override fun getName(): String = name.toLowerCase()

  override fun getMetricHelp(): String = help

  override fun getMetricType(): MetricType = type

  override fun getMetric(maindata: Maindata): String = TODO()
}
