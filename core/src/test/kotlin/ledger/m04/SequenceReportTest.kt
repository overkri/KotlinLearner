package ledger.m04

import io.kotest.matchers.shouldBe
import ledger.collections.firstAbove
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

@Tag("m04")
class SequenceReportTest {

    @Test
    fun `stops after the first match`() {
        val visited = AtomicInteger()
        val entries = (1..100).asSequence().map { id ->
            visited.incrementAndGet()
            TestData.entry(id = id.toLong(), amount = TestData.usd(id.toLong() * 100))
        }

        val found = firstAbove(entries, 250)

        found?.id shouldBe 3L
        visited.get() shouldBe 3
    }

    @Test
    fun `returns null when nothing matches`() {
        val visited = AtomicInteger()
        val entries = (1..10).asSequence().map { id ->
            visited.incrementAndGet()
            TestData.entry(id = id.toLong(), amount = TestData.usd(100))
        }

        firstAbove(entries, 1_000) shouldBe null
        visited.get() shouldBe 10
    }

    @Test
    fun `handles an empty sequence`() {
        firstAbove(emptySequence(), 0) shouldBe null
    }
}
