package ledger.m06

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ledger.domain.AccountStatus
import ledger.domain.Currency
import ledger.errors.parseAndWithdraw
import ledger.errors.withdraw
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m06")
class ResultServiceTest {

    @Test
    fun `withdraws money and bumps the version`() {
        val account = TestData.account(balance = TestData.usd(1_000))

        val updated = withdraw(account, TestData.usd(400)).getOrNull()

        updated?.balance shouldBe TestData.usd(600)
        updated?.version shouldBe 1
        account.balance shouldBe TestData.usd(1_000)
    }

    @Test
    fun `rejects a non-positive amount`() {
        val account = TestData.account(balance = TestData.usd(1_000))

        val error = withdraw(account, TestData.usd(0)).exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "amount must be positive"
    }

    @Test
    fun `rejects a non-active account`() {
        val account = TestData.account(
            status = AccountStatus.FROZEN,
            balance = TestData.usd(1_000),
        )

        val error = withdraw(account, TestData.usd(100)).exceptionOrNull()

        error.shouldBeInstanceOf<IllegalStateException>().message shouldBe
            "account is not active"
    }

    @Test
    fun `rejects a currency mismatch`() {
        val account = TestData.account(balance = TestData.usd(1_000))

        val error = withdraw(account, TestData.eur(100)).exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "currency mismatch"
    }

    @Test
    fun `rejects insufficient funds`() {
        val account = TestData.account(balance = TestData.usd(100))

        val error = withdraw(account, TestData.usd(400)).exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "insufficient funds"
    }

    @Test
    fun `parses and withdraws a valid amount`() {
        val account = TestData.account(balance = TestData.usd(1_000))

        parseAndWithdraw(account, "4.00").getOrNull()?.balance shouldBe TestData.usd(600)
    }

    @Test
    fun `rejects an invalid amount string`() {
        val account = TestData.account(balance = TestData.usd(1_000))

        val error = parseAndWithdraw(account, "abc").exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe "invalid amount"
    }

    @Test
    fun `a failed result can be handled without exceptions`() {
        val account = TestData.account(balance = TestData.usd(100))

        val value = withdraw(account, TestData.usd(400)).getOrElse {
            TestData.usd(0)
        }

        value shouldBe TestData.usd(0)
    }
}
