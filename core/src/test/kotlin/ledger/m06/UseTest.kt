package ledger.m06

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.errors.LedgerBatch
import ledger.errors.withLedgerBatch
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m06")
class UseTest {

    @Test
    fun `returns the block result and closes the batch`() {
        val sink = mutableListOf<String>()
        val batch = LedgerBatch(sink)

        val result = withLedgerBatch(batch) {
            it.append("entry-1")
            it.append("entry-2")
            42
        }

        result shouldBe 42
        sink shouldBe listOf("entry-1", "entry-2")
        batch.closed shouldBe true
    }

    @Test
    fun `closes the batch when the block throws`() {
        val batch = LedgerBatch(mutableListOf())

        val error = shouldThrow<IllegalStateException> {
            withLedgerBatch(batch) {
                error("boom")
            }
        }

        error shouldHaveMessage "boom"
        batch.closed shouldBe true
    }

    @Test
    fun `rejects writes after close`() {
        val batch = LedgerBatch(mutableListOf())
        withLedgerBatch(batch) { it.append("one") }

        val error = shouldThrow<IllegalStateException> { batch.append("two") }
        error shouldHaveMessage "batch is closed"
    }
}
