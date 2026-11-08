package ledger.m11

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import ledger.http.configureMetrics
import ledger.http.metricsRoute
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m11")
class MetricsTest {

    @Test
    fun `exposes Prometheus metrics`() = testApplication {
        application {
            val registry = configureMetrics()
            routing {
                metricsRoute(registry)
                get("/ping") { call.respondText("pong") }
            }
        }

        client.get("/ping").status shouldBe HttpStatusCode.OK

        val response = client.get("/metrics")
        val body = response.bodyAsText()

        response.status shouldBe HttpStatusCode.OK
        body shouldContain "jvm_memory_used_bytes"
        body shouldContain "ktor_"
    }
}
