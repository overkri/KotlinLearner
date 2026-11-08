package ledger.m14

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.migration.LegacyPricingEngine
import ledger.migration.PricingEngine
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m14")
class PricingEngineTest {

    private val legacy = LegacyPricingEngine()
    private val kotlin = PricingEngine()

    @Test
    fun `matches the legacy engine for every tier`() {
        listOf("STANDARD", "SILVER", "GOLD").forEach { tier ->
            listOf(1, 3, 17).forEach { quantity ->
                kotlin.priceCents(quantity, 1_000, tier) shouldBe
                    legacy.priceCents(quantity, 1_000, tier)
            }
        }
    }

    @Test
    fun `uses the same integer rounding`() {
        kotlin.priceCents(3, 333, "GOLD") shouldBe legacy.priceCents(3, 333, "GOLD")
        kotlin.priceCents(1, 1, "SILVER") shouldBe legacy.priceCents(1, 1, "SILVER")
    }

    @Test
    fun `rejects a non-positive quantity`() {
        shouldThrow<IllegalArgumentException> {
            legacy.priceCents(0, 100, "STANDARD")
        } shouldHaveMessage "quantity must be positive"

        shouldThrow<IllegalArgumentException> {
            kotlin.priceCents(0, 100, "STANDARD")
        } shouldHaveMessage "quantity must be positive"
    }

    @Test
    fun `rejects an unknown tier`() {
        shouldThrow<IllegalArgumentException> {
            legacy.priceCents(1, 100, "PLATINUM")
        } shouldHaveMessage "unknown tier: PLATINUM"

        shouldThrow<IllegalArgumentException> {
            kotlin.priceCents(1, 100, "PLATINUM")
        } shouldHaveMessage "unknown tier: PLATINUM"
    }

    @Test
    fun `detects multiplication overflow`() {
        shouldThrow<ArithmeticException> {
            legacy.priceCents(2, Long.MAX_VALUE, "STANDARD")
        }

        shouldThrow<ArithmeticException> {
            kotlin.priceCents(2, Long.MAX_VALUE, "STANDARD")
        }
    }
}
