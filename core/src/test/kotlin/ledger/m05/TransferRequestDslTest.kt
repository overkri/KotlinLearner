package ledger.m05

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.domain.AccountId
import ledger.domain.IdempotencyKey
import ledger.domain.TransferRequest
import ledger.functional.transferRequest
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m05")
class TransferRequestDslTest {

    @Test
    fun `builds a full request`() {
        val request = transferRequest {
            from = AccountId("acc-1")
            to = AccountId("acc-2")
            amount = TestData.usd(100)
            idempotencyKey = IdempotencyKey("idem-1")
            note = "rent"
        }

        request shouldBe TransferRequest(
            from = AccountId("acc-1"),
            to = AccountId("acc-2"),
            amount = TestData.usd(100),
            idempotencyKey = IdempotencyKey("idem-1"),
            note = "rent",
        )
    }

    @Test
    fun `note is optional`() {
        val request = transferRequest {
            from = AccountId("acc-1")
            to = AccountId("acc-2")
            amount = TestData.usd(100)
            idempotencyKey = IdempotencyKey("idem-1")
        }

        request.note shouldBe null
    }

    @Test
    fun `requires from`() {
        val error = shouldThrow<IllegalArgumentException> {
            transferRequest {
                to = AccountId("acc-2")
                amount = TestData.usd(100)
                idempotencyKey = IdempotencyKey("idem-1")
            }
        }

        error shouldHaveMessage "from is required"
    }

    @Test
    fun `requires to`() {
        val error = shouldThrow<IllegalArgumentException> {
            transferRequest {
                from = AccountId("acc-1")
                amount = TestData.usd(100)
                idempotencyKey = IdempotencyKey("idem-1")
            }
        }

        error shouldHaveMessage "to is required"
    }

    @Test
    fun `requires amount`() {
        val error = shouldThrow<IllegalArgumentException> {
            transferRequest {
                from = AccountId("acc-1")
                to = AccountId("acc-2")
                idempotencyKey = IdempotencyKey("idem-1")
            }
        }

        error shouldHaveMessage "amount is required"
    }

    @Test
    fun `requires idempotency key`() {
        val error = shouldThrow<IllegalArgumentException> {
            transferRequest {
                from = AccountId("acc-1")
                to = AccountId("acc-2")
                amount = TestData.usd(100)
            }
        }

        error shouldHaveMessage "idempotencyKey is required"
    }
}
