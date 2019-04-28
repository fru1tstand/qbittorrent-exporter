package me.fru1t.qbtexporter

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import me.fru1t.qbtexporter.cli.Flags
import me.fru1t.qbtexporter.exporter.ExporterServer
import me.fru1t.qbtexporter.logger.Logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch

internal class QbtExporterTest {
  @Mock private lateinit var mockExporterServer: ExporterServer
  @Mock private lateinit var mockLogger: Logger
  @Mock private lateinit var mockFlags: Flags
  private lateinit var exporter: QbtExporter

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    whenever(mockFlags.handle()).thenReturn(false)
    exporter =
      QbtExporter(exporterServer = mockExporterServer, logger = mockLogger, flags = mockFlags)
  }

  @Test
  fun run() {
    exporter.start(CountDownLatch(0))

    verify(mockExporterServer).start()
    verify(mockExporterServer).stop()
  }

  @Test
  fun run_withFlags() {
    whenever(mockFlags.handle()).thenReturn(true)
    exporter.start(CountDownLatch(0))

    verifyNoMoreInteractions(mockExporterServer)
  }
}
