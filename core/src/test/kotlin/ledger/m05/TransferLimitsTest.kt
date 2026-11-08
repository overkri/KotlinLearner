package ledger.m05

import io.kotest.matchers.shouldBe
import ledger.functional.TransferLimits
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m05")
class TransferLimitsTest {

    @Test
    fun `starts from the initial limit`() {
        TransferLimits(1_000).maxMinor shouldBe 1_000
    }

    @Test
    fun `accepts a valid limit`() {
        val limits = TransferLimits(1_000)

        limits.maxMinor = 500
        limits.maxMinor shouldBe 500

        limits.maxMinor = 1_000_000
        limits.maxMinor shouldBe 1_000_000
    }

    @Test
    fun `vetoes limits outside the allowed range`() {
        val limits = TransferLimits(1_000)
        limits.maxMinor = 500

        limits.maxMinor = 0
        limits.maxMinor shouldBe 500

        limits.maxMinor = -1
        limits.maxMinor shouldBe 500

        limits.maxMinor = 1_000_001
        limits.maxMinor shouldBe 500
    }

    @Test
    fun `checks whether an amount is allowed`() {
        val limits = TransferLimits(1_000)

        limits.allows(TestData.usd(500)) shouldBe true
        limits.allows(TestData.usd(1_000)) shouldBe true
        limits.allows(TestData.usd(1_001)) shouldBe false
    }
}
