package ledger.m03

import io.kotest.matchers.shouldBe
import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.Money
import ledger.domain.TransferError
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m03")
class TransferErrorTest {

    private val accountId = AccountId("acc-1")

    @Test
    fun `formats account errors`() {
        TransferError.AccountNotFound(accountId).message shouldBe "Account not found: acc-1"
        TransferError.AccountFrozen(accountId).message shouldBe "Account is frozen: acc-1"
    }

    @Test
    fun `formats insufficient funds`() {
        val error = TransferError.InsufficientFunds(
            accountId = accountId,
            available = TestData.usd(500),
            requested = TestData.usd(1_000),
        )

        error.message shouldBe "Insufficient funds: available 500 USD, requested 1000 USD"
    }

    @Test
    fun `formats currency mismatch`() {
        TransferError.CurrencyMismatch(Currency.USD, Currency.EUR).message shouldBe
            "Currency mismatch: USD -> EUR"
    }

    @Test
    fun `formats invalid amount`() {
        TransferError.InvalidAmount(Money(0, Currency.USD)).message shouldBe "Invalid amount: 0 USD"
    }

    @Test
    fun `same account is a singleton error`() {
        TransferError.SameAccount shouldBe TransferError.SameAccount
        TransferError.SameAccount.message shouldBe "Cannot transfer to the same account"
    }

    @Test
    fun `when over sealed errors is exhaustive`() {
        val errors = listOf(
            TransferError.AccountNotFound(accountId),
            TransferError.AccountFrozen(accountId),
            TransferError.InsufficientFunds(accountId, TestData.usd(0), TestData.usd(1)),
            TransferError.CurrencyMismatch(Currency.USD, Currency.EUR),
            TransferError.InvalidAmount(TestData.usd(0)),
            TransferError.SameAccount,
        )

        errors.map(::errorCode) shouldBe listOf(
            "not-found",
            "frozen",
            "funds",
            "currency",
            "amount",
            "same",
        )
    }

    private fun errorCode(error: TransferError): String = when (error) {
        is TransferError.AccountNotFound -> "not-found"
        is TransferError.AccountFrozen -> "frozen"
        is TransferError.InsufficientFunds -> "funds"
        is TransferError.CurrencyMismatch -> "currency"
        is TransferError.InvalidAmount -> "amount"
        TransferError.SameAccount -> "same"
    }
}
