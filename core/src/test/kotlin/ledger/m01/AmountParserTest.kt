package ledger.m01

import io.kotest.matchers.shouldBe
import ledger.basics.parseAmount
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m01")
class AmountParserTest {

    @Test
    fun `parses major and minor units`() {
        parseAmount("12.34") shouldBe 1_234
        parseAmount("12.3") shouldBe 1_230
        parseAmount("12") shouldBe 1_200
        parseAmount("0.05") shouldBe 5
    }

    @Test
    fun `parses negative amounts`() {
        parseAmount("-1.00") shouldBe -100
        parseAmount("-0.05") shouldBe -5
    }

    @Test
    fun `trims surrounding whitespace`() {
        parseAmount(" 12.34 ") shouldBe 1_234
    }

    @Test
    fun `rejects invalid formats`() {
        parseAmount("12.345") shouldBe null
        parseAmount(".") shouldBe null
        parseAmount("12.") shouldBe null
        parseAmount(".5") shouldBe null
        parseAmount("-") shouldBe null
        parseAmount("") shouldBe null
        parseAmount("abc") shouldBe null
        parseAmount("1,23") shouldBe null
        parseAmount("+1.00") shouldBe null
    }
}
