package ledger.m10

import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.TransferError
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.http.AccountDto
import ledger.http.AccountService
import ledger.http.LedgerDependencies
import ledger.http.TransferService
import ledger.http.ledgerModule
import ledger.serialization.ErrorDto
import ledger.serialization.TransferRequestDto
import ledger.serialization.TransferResponseDto
import ledger.serialization.ledgerJson
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m10")
class LedgerApplicationTest {

    private val account1 = TestData.account(id = "acc-1", ownerName = "Alice")
    private val account2 = TestData.account(id = "acc-2", ownerName = "Bob")

    private class FakeAccounts(private val accounts: List<Account>) : AccountService {
        override suspend fun findById(id: AccountId): Account? =
            accounts.firstOrNull { it.id == id }

        override suspend fun findAll(): List<Account> = accounts
    }

    private class FakeTransfers(private val result: TransferResult) : TransferService {
        var lastRequest: TransferRequest? = null

        override suspend fun transfer(request: TransferRequest): TransferResult {
            lastRequest = request
            return result
        }
    }

    private fun dependencies(
        accounts: List<Account> = listOf(account1, account2),
        result: TransferResult = TransferResult.Success(TestData.transfer(id = "tr-1")),
    ): Pair<LedgerDependencies, FakeTransfers> {
        val transfers = FakeTransfers(result)
        return LedgerDependencies(FakeAccounts(accounts), transfers) to transfers
    }

    @Test
    fun `returns health`() = testApplication {
        val (deps, _) = dependencies()
        application { ledgerModule(deps) }

        val response = client.get("/health")

        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe """{"status":"UP"}"""
    }

    @Test
    fun `lists accounts`() = testApplication {
        val (deps, _) = dependencies()
        application { ledgerModule(deps) }
        val client = createClient { install(ContentNegotiation) { json(ledgerJson()) } }

        val accounts: List<AccountDto> = client.get("/accounts").body()

        accounts.map { it.id } shouldBe listOf("acc-1", "acc-2")
    }

    @Test
    fun `returns an account by id`() = testApplication {
        val (deps, _) = dependencies()
        application { ledgerModule(deps) }
        val client = createClient { install(ContentNegotiation) { json(ledgerJson()) } }

        val account: AccountDto = client.get("/accounts/acc-1").body()

        account.id shouldBe "acc-1"
        account.ownerName shouldBe "Alice"
        account.balanceMinor shouldBe 10_000
        account.currency shouldBe "USD"
    }

    @Test
    fun `returns 404 for a missing account`() = testApplication {
        val (deps, _) = dependencies()
        application { ledgerModule(deps) }
        val client = createClient { install(ContentNegotiation) { json(ledgerJson()) } }

        val response = client.get("/accounts/missing")
        val error: ErrorDto = response.body()

        response.status shouldBe HttpStatusCode.NotFound
        error.code shouldBe "account_not_found"
    }

    @Test
    fun `creates a transfer`() = testApplication {
        val (deps, transfers) = dependencies()
        application { ledgerModule(deps) }
        val client = createClient { install(ContentNegotiation) { json(ledgerJson()) } }

        val response = client.post("/transfers") {
            contentType(ContentType.Application.Json)
            setBody(
                TransferRequestDto(
                    from = "acc-1",
                    to = "acc-2",
                    amountMinor = 1_000,
                    currency = "USD",
                    idempotencyKey = "idem-1",
                ),
            )
        }
        val body: TransferResponseDto = response.body()

        response.status shouldBe HttpStatusCode.Created
        body.id shouldBe "tr-1"
        transfers.lastRequest?.amount?.minor shouldBe 1_000
    }

    @Test
    fun `maps a business failure to an HTTP error`() = testApplication {
        val failure = TransferResult.Failure(
            TransferError.InsufficientFunds(
                accountId = AccountId("acc-1"),
                available = TestData.usd(10),
                requested = TestData.usd(1_000),
            ),
        )
        val (deps, _) = dependencies(result = failure)
        application { ledgerModule(deps) }
        val client = createClient { install(ContentNegotiation) { json(ledgerJson()) } }

        val response = client.post("/transfers") {
            contentType(ContentType.Application.Json)
            setBody(TransferRequestDto("acc-1", "acc-2", 1_000, "USD", "idem-1"))
        }
        val error: ErrorDto = response.body()

        response.status shouldBe HttpStatusCode.Conflict
        error.code shouldBe "insufficient_funds"
    }

    @Test
    fun `rejects an invalid DTO`() = testApplication {
        val (deps, _) = dependencies()
        application { ledgerModule(deps) }
        val client = createClient { install(ContentNegotiation) { json(ledgerJson()) } }

        val response = client.post("/transfers") {
            contentType(ContentType.Application.Json)
            setBody(TransferRequestDto(" ", "acc-2", 1_000, "USD", "idem-1"))
        }
        val error: ErrorDto = response.body()

        response.status shouldBe HttpStatusCode.BadRequest
        error.code shouldBe "invalid_request"
    }

    @Test
    fun `rejects malformed JSON`() = testApplication {
        val (deps, _) = dependencies()
        application { ledgerModule(deps) }
        val client = createClient { install(ContentNegotiation) { json(ledgerJson()) } }

        val response = client.post("/transfers") {
            contentType(ContentType.Application.Json)
            setBody("{not json")
        }

        response.status shouldBe HttpStatusCode.BadRequest
    }

    @Test
    fun `returns 404 for an unknown route`() = testApplication {
        val (deps, _) = dependencies()
        application { ledgerModule(deps) }

        client.get("/unknown").status shouldBe HttpStatusCode.NotFound
    }
}
