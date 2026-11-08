package ledger.m11

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import ledger.http.RequestIdKey
import ledger.http.installRequestId
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m11")
class RequestIdTest {

    @Test
    fun `echoes a client request id`() = testApplication {
        application {
            installRequestId()
            routing {
                get("/") { call.respondText(call.attributes[RequestIdKey]) }
            }
        }

        val response = client.get("/") {
            header("X-Request-Id", "client-request-1")
        }

        response.bodyAsText() shouldBe "client-request-1"
        response.headers["X-Request-Id"] shouldBe "client-request-1"
    }

    @Test
    fun `generates a request id when missing`() = testApplication {
        application {
            installRequestId()
            routing {
                get("/") { call.respondText(call.attributes[RequestIdKey]) }
            }
        }

        val response = client.get("/")
        val generated = response.bodyAsText()

        generated.shouldNotBeBlank()
        response.headers["X-Request-Id"] shouldBe generated
    }
}
