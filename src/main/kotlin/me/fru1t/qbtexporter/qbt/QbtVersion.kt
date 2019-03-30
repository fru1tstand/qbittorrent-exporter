package me.fru1t.qbtexporter.qbt

/**
 * Versions of qBittorrent to be used with the [@since] annotation. This version tag denotes the
 * version of qBittorrent that supports the annotated metric. [@since] should be applied to both
 * incoming qBittorrent json fields and outgoing prometheus metrics.
 */
object QbtVersion {
  /** [release-4.1.5](https://github.com/qbittorrent/qBittorrent/releases/tag/release-4.1.5) */
  const val RELEASE_4_1_5: Double = 41.5
}
