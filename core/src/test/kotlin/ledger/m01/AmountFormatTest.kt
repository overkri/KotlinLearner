package ledger.m01

import io.kotest.matchers.shouldBe
import ledger.basics.formatMinor
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m01")
class AmountFormatTest {

    @Test
    fun `formats USD with two decimals`() {
        formatMinor(1_234, "USD") shouldBe "\$12.34"
        formatMinor(5, "USD") shouldBe "\$0.05"
        formatMinor(0, "USD") shouldBe "\$0.00"
    }

    @Test
    fun `uses euro and pound symbols`() {
        formatMinor(1_234, "EUR") shouldBe "€12.34"
        formatMinor(1_234, "GBP") shouldBe "£12.34"
    }

    @Test
    fun `JPY has no minor units`() {
        formatMinor(1_234, "JPY") shouldBe "¥1234"
    }

    @Test
    fun `puts the minus sign before the currency symbol`() {
        formatMinor(-1_234, "USD") shouldBe "-\$12.34"
        formatMinor(-1_234, "EUR") shouldBe "-€12.34"
        formatMinor(-1_234, "JPY") shouldBe "-¥1234"
    }

    @Test
    fun `keeps the currency code for unknown currencies`() {
        formatMinor(1_234, "XYZ") shouldBe "12.34 XYZ"
        formatMinor(-1_234, "xyz") shouldBe "-12.34 xyz"
    }
}
