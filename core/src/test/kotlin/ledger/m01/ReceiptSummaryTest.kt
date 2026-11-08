package ledger.m01

import io.kotest.matchers.shouldBe
import ledger.basics.receiptSummary
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m01")
class ReceiptSummaryTest {

    @Test
    fun `numbers entries and prints the total`() {
        receiptSummary(listOf(1_234, 500), "USD") shouldBe
            "1. \$12.34\n2. \$5.00\nTOTAL: \$17.34"
    }

    @Test
    fun `handles an empty list`() {
        receiptSummary(emptyList(), "USD") shouldBe "TOTAL: \$0.00"
    }

    @Test
    fun `supports negative entries`() {
        receiptSummary(listOf(1_234, -234), "EUR") shouldBe
            "1. €12.34\n2. -€2.34\nTOTAL: €10.00"
    }

    @Test
    fun `uses the requested currency`() {
        receiptSummary(listOf(100, 200), "JPY") shouldBe
            "1. ¥100\n2. ¥200\nTOTAL: ¥300"
    }
}
