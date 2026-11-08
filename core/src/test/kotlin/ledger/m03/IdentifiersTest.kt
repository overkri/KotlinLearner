package ledger.m03

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.domain.AccountId
import ledger.domain.IdempotencyKey
import ledger.domain.TransferId
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m03")
class IdentifiersTest {

    @Test
    fun `accepts valid identifiers`() {
        AccountId("acc-1").value shouldBe "acc-1"
        TransferId("tr-1").value shouldBe "tr-1"
        IdempotencyKey("idem-1").value shouldBe "idem-1"
    }

    @Test
    fun `rejects blank account and transfer ids`() {
        shouldThrow<IllegalArgumentException> { AccountId("") } shouldHaveMessage
            "id must not be blank"
        shouldThrow<IllegalArgumentException> { AccountId("   ") } shouldHaveMessage
            "id must not be blank"
        shouldThrow<IllegalArgumentException> { TransferId(" ") } shouldHaveMessage
            "id must not be blank"
    }

    @Test
    fun `rejects blank idempotency keys`() {
        shouldThrow<IllegalArgumentException> { IdempotencyKey("") } shouldHaveMessage
            "key must not be blank"
        shouldThrow<IllegalArgumentException> { IdempotencyKey("   ") } shouldHaveMessage
            "key must not be blank"
    }

    @Test
    fun `rejects too long idempotency keys`() {
        val tooLong = "a".repeat(65)

        shouldThrow<IllegalArgumentException> { IdempotencyKey(tooLong) } shouldHaveMessage
            "key is too long"
    }

    @Test
    fun `rejects whitespace inside idempotency keys`() {
        shouldThrow<IllegalArgumentException> { IdempotencyKey("idem 1") } shouldHaveMessage
            "key must not contain whitespace"
    }

    @Test
    fun `accepts a key of maximum length`() {
        IdempotencyKey("a".repeat(64)).value.length shouldBe 64
    }

    @Test
    fun `value classes compare by value`() {
        AccountId("acc-1") shouldBe AccountId("acc-1")
        AccountId("acc-1") shouldNotBe AccountId("acc-2")
        TransferId("tr-1") shouldBe TransferId("tr-1")
    }
}
