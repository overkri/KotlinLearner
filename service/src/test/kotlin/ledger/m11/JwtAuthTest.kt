package ledger.m11

import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import ledger.http.configureJwt
import ledger.http.protected
import ledger.testing.TestJwt
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m11")
class JwtAuthTest {

    @Test
    fun `accepts a valid token`() = testApplication {
        application {
            configureJwt(TestJwt.verifier())
            routing {
                protected {
                    get("/secure") { call.respondText("ok") }
                }
            }
        }

        val response = client.get("/secure") {
            header(HttpHeaders.Authorization, "Bearer ${TestJwt.token()}")
        }

        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe "ok"
    }

    @Test
    fun `rejects a missing token`() = testApplication {
        application {
            configureJwt(TestJwt.verifier())
            routing {
                protected {
                    get("/secure") { call.respondText("ok") }
                }
            }
        }

        client.get("/secure").status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `rejects an expired token`() = testApplication {
        application {
            configureJwt(TestJwt.verifier())
            routing {
                protected {
                    get("/secure") { call.respondText("ok") }
                }
            }
        }

        val response = client.get("/secure") {
            header(
                HttpHeaders.Authorization,
                "Bearer ${TestJwt.token(expiresAt = Instant.now().minusSeconds(60))}",
            )
        }

        response.status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `rejects a token for another audience`() = testApplication {
        application {
            configureJwt(TestJwt.verifier())
            routing {
                protected {
                    get("/secure") { call.respondText("ok") }
                }
            }
        }

        val response = client.get("/secure") {
            header(
                HttpHeaders.Authorization,
                "Bearer ${TestJwt.token(audience = "another-api")}",
            )
        }

        response.status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `rejects a token from another issuer`() = testApplication {
        application {
            configureJwt(TestJwt.verifier())
            routing {
                protected {
                    get("/secure") { call.respondText("ok") }
                }
            }
        }

        val response = client.get("/secure") {
            header(
                HttpHeaders.Authorization,
                "Bearer ${TestJwt.token(issuer = "https://evil-issuer")}",
            )
        }

        response.status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `rejects a token without subject`() = testApplication {
        application {
            configureJwt(TestJwt.verifier())
            routing {
                protected {
                    get("/secure") { call.respondText("ok") }
                }
            }
        }

        val response = client.get("/secure") {
            header(HttpHeaders.Authorization, "Bearer ${TestJwt.token(subject = "")}")
        }

        response.status shouldBe HttpStatusCode.Unauthorized
    }
}
