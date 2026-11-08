package ledger.m00

import io.kotest.matchers.shouldBe
import ledger.legacy.LegacyFeeCalculator
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m00")
class LegacyFeeCalculatorTest {

    private val legacy = LegacyFeeCalculator()

    @Test
    fun `Java legacy code is visible from the Kotlin test source set`() {
        legacy.feeFor(10_000, "USD") shouldBe 30L
    }

    @Test
    fun `JPY keeps zero fee and unknown currencies use the default band`() {
        legacy.feeFor(10_000, "JPY") shouldBe 0L
        legacy.feeFor(10_000, "XYZ") shouldBe 50L
    }
}
