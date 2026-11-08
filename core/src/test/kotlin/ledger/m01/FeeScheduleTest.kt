package ledger.m01

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.basics.feeBps
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m01")
class FeeScheduleTest {

    @Test
    fun `USD has three bands`() {
        feeBps("USD", 999) shouldBe 50
        feeBps("USD", 1_000) shouldBe 25
        feeBps("USD", 9_999) shouldBe 25
        feeBps("USD", 10_000) shouldBe 10
    }

    @Test
    fun `EUR switches at five thousand`() {
        feeBps("EUR", 4_999) shouldBe 40
        feeBps("EUR", 5_000) shouldBe 20
    }

    @Test
    fun `GBP and JPY have flat rules`() {
        feeBps("GBP", 1) shouldBe 30
        feeBps("JPY", 1_000_000) shouldBe 0
    }

    @Test
    fun `unknown currency uses the default band`() {
        feeBps("XYZ", 1_000_000) shouldBe 100
    }

    @Test
    fun `rejects a negative amount`() {
        val error = shouldThrow<IllegalArgumentException> { feeBps("USD", -1) }
        error shouldHaveMessage "amount must be non-negative"
    }
}
