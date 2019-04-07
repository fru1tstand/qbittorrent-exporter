package me.fru1t.qbtexporter.qbt.api

import me.fru1t.qbtexporter.qbt.response.Maindata

/** Methods to query the qBittorrent api. */
interface QbtApi {
  /**
   * Obtains the response to `maindata` as a [Maindata] object. Main data includes things like
   * torrent statuses, categories, and server metrics.
   */
  fun fetchMaindata(): Maindata
}
