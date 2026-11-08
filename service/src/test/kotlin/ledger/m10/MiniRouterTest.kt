package ledger.m10

import io.kotest.matchers.shouldBe
import ledger.http.MiniRequest
import ledger.http.MiniRouter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m10")
class MiniRouterTest {

    private val router = MiniRouter()
        .register("GET", "/health", "health")
        .register("GET", "/accounts/{id}", "accountById")
        .register("GET", "/accounts/{id}/entries/{entryId}", "accountEntry")
        .register("POST", "/transfers", "createTransfer")

    @Test
    fun `matches a static route`() {
        router.route(MiniRequest("GET", "/health")) shouldBe
            ledger.http.RouteMatch("health", emptyMap())
    }

    @Test
    fun `extracts path parameters`() {
        router.route(MiniRequest("GET", "/accounts/acc-1")) shouldBe
            ledger.http.RouteMatch("accountById", mapOf("id" to "acc-1"))
    }

    @Test
    fun `extracts several path parameters`() {
        router.route(MiniRequest("GET", "/accounts/acc-1/entries/42")) shouldBe
            ledger.http.RouteMatch(
                "accountEntry",
                mapOf("id" to "acc-1", "entryId" to "42"),
            )
    }

    @Test
    fun `matches the method case-insensitively`() {
        router.route(MiniRequest("get", "/health"))?.handler shouldBe "health"
    }

    @Test
    fun `rejects a wrong method`() {
        router.route(MiniRequest("POST", "/health")) shouldBe null
    }

    @Test
    fun `rejects extra or missing segments`() {
        router.route(MiniRequest("GET", "/accounts/acc-1/extra")) shouldBe null
        router.route(MiniRequest("GET", "/accounts")) shouldBe null
    }

    @Test
    fun `rejects an unknown path`() {
        router.route(MiniRequest("GET", "/unknown")) shouldBe null
    }

    @Test
    fun `uses the first registered matching route`() {
        val ordered = MiniRouter()
            .register("GET", "/accounts/{id}", "byId")
            .register("GET", "/accounts/me", "me")

        ordered.route(MiniRequest("GET", "/accounts/me"))?.handler shouldBe "byId"
    }
}
