package me.fru1t.qbtexporter.collector

import me.fru1t.qbtexporter.prometheus.Metric
import me.fru1t.qbtexporter.qbt.response.Maindata

/** Represents a collector that consumes [Maindata] and outputs a [Metric]. */
interface MaindataCollector {
  /** Returns the [Metric] collected by consuming [maindata]. */
  fun collect(maindata: Maindata): Metric

  /** Retrieves the name of this metric. */
  fun getName(): String
}
