package ledger.m09

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.IdempotencyKey
import ledger.domain.Money
import ledger.domain.TransferError
import ledger.domain.TransferRequest
import ledger.serialization.ErrorDto
import ledger.serialization.TransferRequestDto
import ledger.serialization.toDomain
import ledger.serialization.toDto
import ledger.serialization.toErrorDto
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m09")
class TransferDtoTest {

    @Test
    fun `maps a request DTO to the domain`() {
        val dto = TransferRequestDto(
            from = "acc-1",
            to = "acc-2",
            amountMinor = 1_000,
            currency = "USD",
            idempotencyKey = "idem-1",
            note = "rent",
        )

        dto.toDomain().getOrNull() shouldBe TransferRequest(
            from = AccountId("acc-1"),
            to = AccountId("acc-2"),
            amount = Money(1_000, Currency.USD),
            idempotencyKey = IdempotencyKey("idem-1"),
            note = "rent",
        )
    }

    @Test
    fun `rejects an unknown currency`() {
        val dto = TransferRequestDto("a", "b", 100, "XYZ", "idem-1")

        val error = dto.toDomain().exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "invalid currency: XYZ"
    }

    @Test
    fun `rejects blank identifiers`() {
        val dto = TransferRequestDto(" ", "b", 100, "USD", "idem-1")

        val error = dto.toDomain().exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "id must not be blank"
    }

    @Test
    fun `rejects a negative amount`() {
        val dto = TransferRequestDto("a", "b", -1, "USD", "idem-1")

        val error = dto.toDomain().exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "minor must be non-negative"
    }

    @Test
    fun `rejects a blank idempotency key`() {
        val dto = TransferRequestDto("a", "b", 100, "USD", " ")

        val error = dto.toDomain().exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "key must not be blank"
    }

    @Test
    fun `maps a transfer to a response DTO`() {
        TestData.transfer(
            id = "tr-1",
            from = "acc-1",
            to = "acc-2",
            amount = TestData.usd(1_000),
        ).toDto() shouldBe ledger.serialization.TransferResponseDto(
            id = "tr-1",
            from = "acc-1",
            to = "acc-2",
            amountMinor = 1_000,
            currency = "USD",
            status = "COMPLETED",
            idempotencyKey = "idem-1",
        )
    }

    @Test
    fun `maps every error to an API code`() {
        val account = AccountId("acc-1")
        val errors = listOf(
            TransferError.AccountNotFound(account) to "account_not_found",
            TransferError.AccountFrozen(account) to "account_frozen",
            TransferError.InsufficientFunds(account, TestData.usd(1), TestData.usd(2)) to "insufficient_funds",
            TransferError.CurrencyMismatch(Currency.USD, Currency.EUR) to "currency_mismatch",
            TransferError.InvalidAmount(TestData.usd(0)) to "invalid_amount",
            TransferError.SameAccount to "same_account",
        )

        errors.forEach { (error, code) ->
            error.toErrorDto() shouldBe ErrorDto(code = code, message = error.message)
        }
    }
}
