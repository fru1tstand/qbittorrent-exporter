package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.qbt.response.Maindata

/**
 * A container that holds a multitude of maindata collectors. This interface should be applied
 * to the container's companion object.
 */
interface MaindataCollectorContainer {
  /** Collect all metrics from all collectors within this container. */
  fun collect(settings: MaindataCollectorContainerSettings, maindata: Maindata): List<Metric>
}
