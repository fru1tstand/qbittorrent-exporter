package me.fru1t.qbtexporter.exporter.impl

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import me.fru1t.qbtexporter.collector.CollectorSettingsUtils
import me.fru1t.qbtexporter.exporter.ExporterServer
import me.fru1t.qbtexporter.qbt.api.QbtApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/** Implementation of [ExporterServer]. */
class ExporterServerImpl @Inject constructor(
  private val qbtApi: QbtApi,
  private val collectorSettingsUtils: CollectorSettingsUtils
) : ExporterServer {
  private val server: ApplicationEngine = embeddedServer(factory = Jetty, port = 9561) {
    routing {
      get("/") {
        call.respondText(
          text = "<a href=\"/metrics\">metrics</a>",
          contentType = ContentType.Text.Html
        )
      }
      get("metrics") {
        val maindata = qbtApi.fetchMaindata()
        val metrics = ArrayList<String>()
        collectorSettingsUtils.getEnabledMaindataCollectors().forEach { collector ->
          metrics.add(collector.collect(maindata).toString())
        }
        call.respondText(
          text = metrics.joinToString(separator = "\n"),
          contentType = ContentType.Text.Plain
        )
      }
    }
  }

  override fun start() {
    server.start()
  }

  override fun stop() {
    server.stop(gracePeriod = 1000, timeout = 2000, timeUnit = TimeUnit.SECONDS)
  }
}
