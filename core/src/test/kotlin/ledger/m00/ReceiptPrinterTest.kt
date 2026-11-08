package ledger.m00

import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ledger.basics.Clock
import ledger.basics.ReceiptPrinter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m00")
class ReceiptPrinterTest {

    private val clock = mockk<Clock>()
    private val printer = ReceiptPrinter(clock)

    @Test
    fun `formats amount, currency and injected time`() {
        every { clock.now() } returns Instant.parse("2026-11-08T12:00:00Z")

        printer.print(1_234, "USD") shouldBe "1234 USD @ 2026-11-08T12:00:00Z"
    }

    @Test
    fun `reads the clock exactly once`() {
        every { clock.now() } returns Instant.parse("2026-11-08T12:00:00Z")

        printer.print(1_234, "EUR")

        verify(exactly = 1) { clock.now() }
    }

    @Test
    fun `rejects a negative amount`() {
        val error = shouldThrow<IllegalArgumentException> {
            printer.print(-1, "USD")
        }

        error shouldHaveMessage "amount must be positive"
    }
}
