package ledger.m11

import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import ledger.http.HealthCheck
import ledger.http.healthRoutes
import ledger.serialization.ledgerJson
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m11")
class HealthRoutesTest {

    @Test
    fun `liveness is always up`() = testApplication {
        application {
            install(ContentNegotiation) { json(ledgerJson()) }
            routing { healthRoutes(HealthCheck { false }) }
        }

        val response = client.get("/health/live")

        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe """{"status":"UP"}"""
    }

    @Test
    fun `readiness is up when the service is ready`() = testApplication {
        application {
            install(ContentNegotiation) { json(ledgerJson()) }
            routing { healthRoutes(HealthCheck { true }) }
        }

        val response = client.get("/health/ready")

        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe """{"status":"UP"}"""
    }

    @Test
    fun `readiness is down when the service is not ready`() = testApplication {
        application {
            install(ContentNegotiation) { json(ledgerJson()) }
            routing { healthRoutes(HealthCheck { false }) }
        }

        val response = client.get("/health/ready")

        response.status shouldBe HttpStatusCode.ServiceUnavailable
        response.bodyAsText() shouldBe """{"status":"DOWN"}"""
    }
}
