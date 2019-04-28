package me.fru1t.qbtexporter

import com.nhaarman.mockitokotlin2.verify
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
  private lateinit var exporter: QbtExporter

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.initMocks(this)

    exporter = QbtExporter(exporterServer = mockExporterServer, logger = mockLogger)
  }

  @Test
  fun run() {
    val cdl = CountDownLatch(0)
    exporter.start(cdl)

    verify(mockExporterServer).start()
    verify(mockExporterServer).stop()
  }
}
