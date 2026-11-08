package ledger.m10

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.TransferError
import ledger.http.toHttpStatus
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m10")
class ErrorMappingTest {

    private val account = AccountId("acc-1")

    @Test
    fun `maps every domain error to an HTTP status`() {
        val errors = mapOf(
            TransferError.AccountNotFound(account) to HttpStatusCode.NotFound,
            TransferError.AccountFrozen(account) to HttpStatusCode.Conflict,
            TransferError.InsufficientFunds(account, TestData.usd(1), TestData.usd(2)) to
                HttpStatusCode.Conflict,
            TransferError.CurrencyMismatch(Currency.USD, Currency.EUR) to
                HttpStatusCode.UnprocessableEntity,
            TransferError.InvalidAmount(TestData.usd(0)) to HttpStatusCode.BadRequest,
            TransferError.SameAccount to HttpStatusCode.BadRequest,
        )

        errors.forEach { (error, status) ->
            error.toHttpStatus() shouldBe status
        }
    }
}
