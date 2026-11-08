package ledger.m00

import io.kotest.matchers.shouldBe
import ledger.basics.FeeCalculatorBridge
import ledger.legacy.LegacyFeeCalculator
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m00")
class FeeCalculatorBridgeTest {

    private val bridge = FeeCalculatorBridge()
    private val legacy = LegacyFeeCalculator()

    @Test
    fun `uses USD when currency is omitted`() {
        bridge.feeFor(1_000_000) shouldBe legacy.feeFor(1_000_000, "USD")
    }

    @Test
    fun `delegates every currency to the Java implementation`() {
        bridge.feeFor(1_000_000, "EUR") shouldBe legacy.feeFor(1_000_000, "EUR")
        bridge.feeFor(1_000_000, "GBP") shouldBe legacy.feeFor(1_000_000, "GBP")
        bridge.feeFor(1_000_000, "JPY") shouldBe 0L
    }

    @Test
    fun `calls the Java one-argument overload for USD`() {
        bridge.feeForUsd(1_000_000) shouldBe legacy.feeFor(1_000_000)
    }

    @Test
    fun `describes known currencies`() {
        bridge.describe("USD") shouldBe "US dollar"
        bridge.describe("EUR") shouldBe "Euro"
    }

    @Test
    fun `never exposes a platform null`() {
        bridge.describe("XYZ") shouldBe "Unknown currency"
    }
}
