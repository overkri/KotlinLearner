package ledger.m03

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.domain.Account
import ledger.domain.AccountStatus
import ledger.domain.Currency
import ledger.domain.Money
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m03")
class AccountTest {

    @Test
    fun `rejects a blank owner name`() {
        val error = shouldThrow<IllegalArgumentException> {
            TestData.account(ownerName = "   ")
        }

        error shouldHaveMessage "ownerName must not be blank"
    }

    @Test
    fun `rejects a negative version`() {
        val error = shouldThrow<IllegalArgumentException> {
            TestData.account(version = -1)
        }

        error shouldHaveMessage "version must be non-negative"
    }

    @Test
    fun `freezes an active account and bumps the version`() {
        val active = TestData.account(status = AccountStatus.ACTIVE)

        val frozen = active.freeze()

        frozen.status shouldBe AccountStatus.FROZEN
        frozen.version shouldBe 1
        active.status shouldBe AccountStatus.ACTIVE
        active.version shouldBe 0
    }

    @Test
    fun `freezing a frozen account is idempotent`() {
        val frozen = TestData.account(status = AccountStatus.FROZEN)

        (frozen.freeze() === frozen) shouldBe true
        frozen.freeze().version shouldBe 0
    }

    @Test
    fun `cannot freeze a closed account`() {
        val closed = TestData.account(status = AccountStatus.CLOSED)

        val error = shouldThrow<IllegalArgumentException> { closed.freeze() }
        error shouldHaveMessage "closed account cannot be frozen"
    }

    @Test
    fun `activates a frozen account`() {
        val frozen = TestData.account(status = AccountStatus.FROZEN)

        val active = frozen.activate()

        active.status shouldBe AccountStatus.ACTIVE
        active.version shouldBe 1
    }

    @Test
    fun `activating an active account is idempotent`() {
        val active = TestData.account(status = AccountStatus.ACTIVE)

        (active.activate() === active) shouldBe true
    }

    @Test
    fun `cannot activate a closed account`() {
        val closed = TestData.account(status = AccountStatus.CLOSED)

        val error = shouldThrow<IllegalArgumentException> { closed.activate() }
        error shouldHaveMessage "closed account cannot be activated"
    }

    @Test
    fun `closes an account with zero balance`() {
        val empty = TestData.account(balance = TestData.usd(0))

        val closed = empty.close()

        closed.status shouldBe AccountStatus.CLOSED
        closed.version shouldBe 1
    }

    @Test
    fun `cannot close an account with money`() {
        val funded = TestData.account(balance = TestData.usd(100))

        val error = shouldThrow<IllegalArgumentException> { funded.close() }
        error shouldHaveMessage "account with non-zero balance cannot be closed"
    }

    @Test
    fun `closing a closed account is idempotent`() {
        val closed = TestData.account(status = AccountStatus.CLOSED, balance = TestData.usd(0))

        (closed.close() === closed) shouldBe true
    }

    @Test
    fun `withBalance replaces the amount and bumps the version`() {
        val account = TestData.account(balance = TestData.usd(100_00))

        val updated = account.withBalance(TestData.usd(250_00))

        updated.balance shouldBe TestData.usd(250_00)
        updated.version shouldBe 1
        account.balance shouldBe TestData.usd(100_00)
    }

    @Test
    fun `withBalance rejects a different currency`() {
        val account = TestData.account(balance = TestData.usd(100_00))

        val error = shouldThrow<IllegalArgumentException> {
            account.withBalance(Money(100_00, Currency.EUR))
        }
        error shouldHaveMessage "currency mismatch"
    }
}
