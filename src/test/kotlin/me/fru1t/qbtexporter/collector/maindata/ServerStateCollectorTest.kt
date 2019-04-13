package me.fru1t.qbtexporter.collector.maindata

import com.google.common.truth.Truth.assertThat
import me.fru1t.qbtexporter.qbt.response.Maindata
import me.fru1t.qbtexporter.qbt.response.maindata.ServerState
import org.junit.jupiter.api.Test

internal class ServerStateCollectorTest {
  private companion object {
    private val TEST_SERVER_STATE = ServerState(
      allTimeDownloadedBytes = 1,
      allTimeUploadedBytes = 2,
      allTimeShareRatio = "3.33",
      diskAverageCacheQueueTimeMs = 4,
      diskCacheSizeBytes = 5,
      diskFreeSpaceBytes = 6,
      diskQueueJobCount = 7,
      diskQueueSizeBytes = 8,
      diskReadCacheHitPercent = "9.99",
      diskReadCacheOverloadPercent = "10.10",
      diskWriteCacheOverloadPercent = "11.11",
      networkDhtNodeCount = 12,
      currentSessionDownloadedBytes = 13,
      currentSessionUploadedBytes = 14,
      currentSessionBytesWasted = 15,
      networkPeerConnectionCount = 16,
      networkDownloadSpeedBytesPerSecond = 17,
      networkDownloadSpeedCapBytesPerSecond = 18,
      networkUploadSpeedBytesPerSecond = 19,
      networkUploadSpeedCapBytesPerSecond = 20
    )
    private val TEST_DATA = Maindata(serverState = TEST_SERVER_STATE)

    /** Returns the metric name for the [serverStateCollector]. */
    private fun metricNameOf(serverStateCollector: ServerStateCollector): String =
      "qbt_server_state_${serverStateCollector.name.toLowerCase()}"

    /**
     * Asserts that when passing [TEST_DATA] into the [serverStateCollector], the [expectedOutput]
     * is produced.
     */
    private fun assertOutput(serverStateCollector: ServerStateCollector, expectedOutput: Number) {
      val resultInternalMetric = serverStateCollector.collect(TEST_DATA).toString().lines().last()

      assertThat(resultInternalMetric)
        .isEqualTo("${metricNameOf(serverStateCollector)} $expectedOutput")
    }
  }

  @Test
  fun collect_returnsNull_whenNotFoundInMaindata() {
    val emptyMaindata = Maindata()

    ServerStateCollector.values().forEach { collector ->
      assertThat(collector.collect(emptyMaindata).toString().lines().last())
        .isEqualTo("${metricNameOf(collector)} 0")
    }
  }

  @Test
  fun collect_allTimeDownloadBytes() {
    assertOutput(
      ServerStateCollector.ALL_TIME_DOWNLOAD_BYTES, TEST_SERVER_STATE.allTimeDownloadedBytes!!
    )
  }

  @Test
  fun collect_allTimeUploadBytes() {
    assertOutput(
      ServerStateCollector.ALL_TIME_UPLOAD_BYTES, TEST_SERVER_STATE.allTimeUploadedBytes!!
    )
  }

  @Test
  fun collect_diskAverageCacheQueueTimeMs() {
    assertOutput(
      ServerStateCollector.DISK_AVERAGE_CACHE_QUEUE_TIME_MS,
      TEST_SERVER_STATE.diskAverageCacheQueueTimeMs!!
    )
  }

  @Test
  fun collect_diskCacheSizeBytes() {
    assertOutput(ServerStateCollector.DISK_CACHE_SIZE_BYTES, TEST_SERVER_STATE.diskCacheSizeBytes!!)
  }

  @Test
  fun collect_diskFreeSpaceBytes() {
    assertOutput(ServerStateCollector.DISK_FREE_SPACE_BYTES, TEST_SERVER_STATE.diskFreeSpaceBytes!!)
  }

  @Test
  fun collect_diskQueueJobCount() {
    assertOutput(ServerStateCollector.DISK_QUEUE_JOB_COUNT, TEST_SERVER_STATE.diskQueueJobCount!!)
  }

  @Test
  fun collect_diskQueueSizeBytes() {
    assertOutput(ServerStateCollector.DISK_QUEUE_SIZE_BYTES, TEST_SERVER_STATE.diskQueueSizeBytes!!)
  }

  @Test
  fun collect_diskReadCacheHitPercent() {
    assertOutput(
      ServerStateCollector.DISK_READ_CACHE_HIT_PERCENT,
      TEST_SERVER_STATE.diskReadCacheHitPercent!!.toDouble()
    )
  }

  @Test
  fun collect_diskReadCacheOverloadPercent() {
    assertOutput(
      ServerStateCollector.DISK_READ_CACHE_OVERLOAD_PERCENT,
      TEST_SERVER_STATE.diskReadCacheOverloadPercent!!.toDouble()
    )
  }

  @Test
  fun collect_diskWriteCacheOverloadPercent() {
    assertOutput(
      ServerStateCollector.DISK_WRITE_CACHE_OVERLOAD_PERCENT,
      TEST_SERVER_STATE.diskWriteCacheOverloadPercent!!.toDouble()
    )
  }

  @Test
  fun collect_networkDhtNodeCount() {
    assertOutput(
      ServerStateCollector.NETWORK_DHT_NODE_COUNT,
      TEST_SERVER_STATE.networkDhtNodeCount!!
    )
  }

  @Test
  fun collect_currentSessionDownloadedBytes() {
    assertOutput(
      ServerStateCollector.CURRENT_SESSION_DOWNLOADED_BYTES,
      TEST_SERVER_STATE.currentSessionDownloadedBytes!!
    )
  }

  @Test
  fun collect_currentSessionUploadBytes() {
    assertOutput(
      ServerStateCollector.CURRENT_SESSION_UPLOADED_BYTES,
      TEST_SERVER_STATE.currentSessionUploadedBytes!!
    )
  }

  @Test
  fun collect_currentSessionWastedBytes() {
    assertOutput(
      ServerStateCollector.CURRENT_SESSION_WASTED_BYTES,
      TEST_SERVER_STATE.currentSessionBytesWasted!!
    )
  }

  @Test
  fun collect_networkPeerConnectionCount() {
    assertOutput(
      ServerStateCollector.NETWORK_PEER_CONNECTION_COUNT,
      TEST_SERVER_STATE.networkPeerConnectionCount!!
    )
  }

  @Test
  fun collect_networkDownloadSpeedBytesPerSecond() {
    assertOutput(
      ServerStateCollector.NETWORK_DOWNLOAD_SPEED_BYTES_PER_SECOND,
      TEST_SERVER_STATE.networkDownloadSpeedBytesPerSecond!!
    )
  }

  @Test
  fun collect_networkDownloadSpeedCapBytesPerSecond() {
    assertOutput(
      ServerStateCollector.NETWORK_DOWNLOAD_SPEED_CAP_BYTES_SER_SECOND,
      TEST_SERVER_STATE.networkDownloadSpeedCapBytesPerSecond!!
    )
  }

  @Test
  fun collect_networkUploadSpeedBytesPerSecond() {
    assertOutput(
      ServerStateCollector.NETWORK_UPLOAD_SPEED_BYTES_PER_SECOND,
      TEST_SERVER_STATE.networkUploadSpeedBytesPerSecond!!
    )
  }

  @Test
  fun collect_networkUploadSpeedCapBytesPerSecond() {
    assertOutput(
      ServerStateCollector.NETWORK_UPLOAD_SPEED_CAP_BYTES_PER_SECOND,
      TEST_SERVER_STATE.networkUploadSpeedCapBytesPerSecond!!
    )
  }
}
