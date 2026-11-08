package ledger.m06

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ledger.domain.Account
import ledger.domain.AccountStatus
import ledger.domain.Currency
import ledger.domain.IdempotencyKey
import ledger.domain.Money
import ledger.domain.Transfer
import ledger.domain.TransferError
import ledger.domain.TransferId
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.domain.TransferStatus
import ledger.errors.TransferEngine
import ledger.testing.InMemoryAccountRepository
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m06")
class TransferEngineTest {

    private val fixed: Instant = Instant.parse("2026-02-01T09:00:00Z")
    private val key = IdempotencyKey("idem-1")

    private fun engine(vararg accounts: Account): Pair<TransferEngine, InMemoryAccountRepository> {
        val repo = InMemoryAccountRepository(accounts.toList())
        val engine = TransferEngine(repo, { TransferId("tr-test") }, { fixed })
        return engine to repo
    }

    private fun request(from: Account, to: Account, amount: Money = TestData.usd(400)): TransferRequest =
        TransferRequest(from.id, to.id, amount, key)

    private fun error(result: TransferResult): TransferError =
        result.shouldBeInstanceOf<TransferResult.Failure>().error

    @Test
    fun `moves money and persists both accounts`() {
        val from = TestData.account(id = "from", balance = TestData.usd(1_000))
        val to = TestData.account(id = "to", balance = TestData.usd(0))
        val (engine, repo) = engine(from, to)

        val result = engine.transfer(request(from, to))

        result shouldBe TransferResult.Success(
            Transfer(
                id = TransferId("tr-test"),
                from = from.id,
                to = to.id,
                amount = TestData.usd(400),
                status = TransferStatus.COMPLETED,
                idempotencyKey = key,
                createdAt = fixed,
            ),
        )
        repo.findById(from.id)?.balance shouldBe TestData.usd(600)
        repo.findById(to.id)?.balance shouldBe TestData.usd(400)
        repo.findById(from.id)?.version shouldBe 1
        repo.findById(to.id)?.version shouldBe 1
        repo.saves shouldBe 2
    }

    @Test
    fun `rejects a non-positive amount`() {
        val from = TestData.account(id = "from")
        val to = TestData.account(id = "to")
        val (engine, repo) = engine(from, to)

        error(engine.transfer(request(from, to, TestData.usd(0)))) shouldBe
            TransferError.InvalidAmount(TestData.usd(0))
        repo.saves shouldBe 0
    }

    @Test
    fun `rejects a transfer to the same account`() {
        val account = TestData.account(id = "same")
        val (engine, repo) = engine(account)

        error(engine.transfer(request(account, account))) shouldBe TransferError.SameAccount
        repo.saves shouldBe 0
    }

    @Test
    fun `reports a missing source account`() {
        val from = TestData.account(id = "missing")
        val to = TestData.account(id = "to")
        val (engine, repo) = engine(to)

        error(engine.transfer(request(from, to))) shouldBe TransferError.AccountNotFound(from.id)
        repo.saves shouldBe 0
    }

    @Test
    fun `reports a missing target account`() {
        val from = TestData.account(id = "from")
        val to = TestData.account(id = "missing")
        val (engine, repo) = engine(from)

        error(engine.transfer(request(from, to))) shouldBe TransferError.AccountNotFound(to.id)
        repo.saves shouldBe 0
    }

    @Test
    fun `rejects a frozen source account`() {
        val from = TestData.account(id = "from", status = AccountStatus.FROZEN)
        val to = TestData.account(id = "to")
        val (engine, repo) = engine(from, to)

        error(engine.transfer(request(from, to))) shouldBe TransferError.AccountFrozen(from.id)
        repo.saves shouldBe 0
    }

    @Test
    fun `rejects a frozen target account`() {
        val from = TestData.account(id = "from")
        val to = TestData.account(id = "to", status = AccountStatus.FROZEN)
        val (engine, repo) = engine(from, to)

        error(engine.transfer(request(from, to))) shouldBe TransferError.AccountFrozen(to.id)
        repo.saves shouldBe 0
    }

    @Test
    fun `rejects a currency mismatch on the source account`() {
        val from = TestData.account(id = "from", balance = TestData.usd(1_000))
        val to = TestData.account(id = "to", balance = TestData.usd(0))
        val (engine, repo) = engine(from, to)

        error(engine.transfer(request(from, to, TestData.eur(100)))) shouldBe
            TransferError.CurrencyMismatch(Currency.USD, Currency.EUR)
        repo.saves shouldBe 0
    }

    @Test
    fun `rejects a currency mismatch on the target account`() {
        val from = TestData.account(id = "from", balance = TestData.usd(1_000))
        val to = TestData.account(id = "to", balance = TestData.eur(0))
        val (engine, repo) = engine(from, to)

        error(engine.transfer(request(from, to, TestData.usd(100)))) shouldBe
            TransferError.CurrencyMismatch(Currency.EUR, Currency.USD)
        repo.saves shouldBe 0
    }

    @Test
    fun `rejects insufficient funds`() {
        val from = TestData.account(id = "from", balance = TestData.usd(100))
        val to = TestData.account(id = "to", balance = TestData.usd(0))
        val (engine, repo) = engine(from, to)

        error(engine.transfer(request(from, to, TestData.usd(400)))) shouldBe
            TransferError.InsufficientFunds(from.id, TestData.usd(100), TestData.usd(400))
        repo.saves shouldBe 0
    }
}
