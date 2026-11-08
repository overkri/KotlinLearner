package ledger.m09

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.config.loadConfig
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m09")
class AppConfigTest {

    private val required = mapOf(
        "DB_URL" to "jdbc:postgresql://localhost:5432/ledger",
        "JWT_ISSUER" to "https://keycloak/realms/ledger",
        "JWT_AUDIENCE" to "ledger-api",
        "JWT_JWKS_URL" to "https://keycloak/realms/ledger/protocol/openid-connect/certs",
    )

    @Test
    fun `uses defaults for optional values`() {
        val config = loadConfig(required)

        config.http shouldBe ledger.config.HttpConfig("0.0.0.0", 8080)
        config.db shouldBe ledger.config.DbConfig(
            url = "jdbc:postgresql://localhost:5432/ledger",
            user = "ledger",
            password = "ledger",
            poolSize = 10,
        )
        config.jwt.issuer shouldBe "https://keycloak/realms/ledger"
        config.jwt.audience shouldBe "ledger-api"
    }

    @Test
    fun `reads and trims custom values`() {
        val config = loadConfig(
            required + mapOf(
                "HTTP_HOST" to "  localhost  ",
                "HTTP_PORT" to " 9090 ",
                "DB_USER" to " app ",
                "DB_PASSWORD" to " secret ",
                "DB_POOL_SIZE" to " 25 ",
            ),
        )

        config.http shouldBe ledger.config.HttpConfig("localhost", 9090)
        config.db.user shouldBe "app"
        config.db.password shouldBe "secret"
        config.db.poolSize shouldBe 25
    }

    @Test
    fun `requires database and jwt settings`() {
        shouldThrow<IllegalArgumentException> { loadConfig(emptyMap()) } shouldHaveMessage
            "DB_URL is required"
        shouldThrow<IllegalArgumentException> { loadConfig(required - "JWT_ISSUER") } shouldHaveMessage
            "JWT_ISSUER is required"
        shouldThrow<IllegalArgumentException> { loadConfig(required - "JWT_AUDIENCE") } shouldHaveMessage
            "JWT_AUDIENCE is required"
        shouldThrow<IllegalArgumentException> { loadConfig(required - "JWT_JWKS_URL") } shouldHaveMessage
            "JWT_JWKS_URL is required"
    }

    @Test
    fun `treats blank values as missing`() {
        shouldThrow<IllegalArgumentException> {
            loadConfig(required + ("DB_URL" to "   "))
        } shouldHaveMessage "DB_URL is required"
    }

    @Test
    fun `validates the http port`() {
        listOf("0", "65536", "abc").forEach { value ->
            shouldThrow<IllegalArgumentException> {
                loadConfig(required + ("HTTP_PORT" to value))
            } shouldHaveMessage "HTTP_PORT must be a valid port"
        }
    }

    @Test
    fun `validates the pool size`() {
        listOf("0", "101", "abc").forEach { value ->
            shouldThrow<IllegalArgumentException> {
                loadConfig(required + ("DB_POOL_SIZE" to value))
            } shouldHaveMessage "DB_POOL_SIZE must be a valid pool size"
        }
    }
}
