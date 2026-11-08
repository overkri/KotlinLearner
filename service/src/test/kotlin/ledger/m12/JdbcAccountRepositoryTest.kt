package ledger.m12

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ledger.domain.AccountId
import ledger.persistence.JdbcAccountRepository
import ledger.persistence.OptimisticLockException
import ledger.testing.PostgresFixture
import ledger.testing.TestData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m12")
@Tag("integration")
class JdbcAccountRepositoryTest {

    private val repository = JdbcAccountRepository(PostgresFixture.dataSource)

    @BeforeEach
    fun reset() {
        PostgresFixture.reset()
    }

    @Test
    fun `saves and finds an account`() {
        val account = TestData.account(
            id = "acc-1",
            ownerName = "Alice",
            status = ledger.domain.AccountStatus.ACTIVE,
            balance = TestData.usd(100_00),
        )

        repository.save(account)

        repository.findById(account.id) shouldBe account
    }

    @Test
    fun `returns null for an unknown account`() {
        repository.findById(AccountId("missing")) shouldBe null
    }

    @Test
    fun `updates an existing account with optimistic locking`() {
        val account = TestData.account(id = "acc-1", balance = TestData.usd(100_00))
        repository.save(account)

        val updated = account.withBalance(TestData.usd(250_00))
        repository.save(updated)

        repository.findById(account.id) shouldBe updated
    }

    @Test
    fun `rejects a stale update`() {
        val account = TestData.account(id = "acc-1", balance = TestData.usd(100_00))
        repository.save(account)
        repository.save(account.withBalance(TestData.usd(250_00)))

        val stale = account.copy(balance = TestData.usd(999_00), version = 1)

        val error = shouldThrow<OptimisticLockException> { repository.save(stale) }
        error.message.shouldContain("acc-1")
    }
}
