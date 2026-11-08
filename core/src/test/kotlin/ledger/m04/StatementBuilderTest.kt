package ledger.m04

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.collections.statement
import ledger.domain.AccountId
import ledger.domain.Direction
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m04")
class StatementBuilderTest {

    private val account = AccountId("acc-1")
    private val t1: Instant = Instant.parse("2026-01-15T10:00:00Z")
    private val t2: Instant = Instant.parse("2026-01-15T11:00:00Z")
    private val t3: Instant = Instant.parse("2026-01-15T12:00:00Z")

    @Test
    fun `filters, sorts and computes the running balance`() {
        val entries = listOf(
            TestData.entry(id = 3, accountId = "acc-1", direction = Direction.CREDIT, amount = TestData.usd(200), createdAt = t3),
            TestData.entry(id = 1, accountId = "acc-1", direction = Direction.DEBIT, amount = TestData.usd(100), createdAt = t1),
            TestData.entry(id = 99, accountId = "acc-2", direction = Direction.CREDIT, amount = TestData.usd(999), createdAt = t2),
            TestData.entry(id = 2, accountId = "acc-1", direction = Direction.CREDIT, amount = TestData.usd(50), createdAt = t2),
        )

        val rows = statement(entries, account)

        rows.map { it.entryId } shouldBe listOf(1, 2, 3)
        rows.map { it.index } shouldBe listOf(1, 2, 3)
        rows.map { it.balanceAfterMinor } shouldBe listOf(-100L, -50L, 150L)
        rows.map { it.direction } shouldBe listOf(Direction.DEBIT, Direction.CREDIT, Direction.CREDIT)
    }

    @Test
    fun `breaks timestamp ties by entry id`() {
        val entries = listOf(
            TestData.entry(id = 10, accountId = "acc-1", createdAt = t1),
            TestData.entry(id = 2, accountId = "acc-1", createdAt = t1),
        )

        statement(entries, account).map { it.entryId } shouldBe listOf(2, 10)
    }

    @Test
    fun `returns an empty statement`() {
        statement(emptyList(), account) shouldBe emptyList()
    }

    @Test
    fun `rejects mixed currencies`() {
        val entries = listOf(
            TestData.entry(id = 1, accountId = "acc-1", amount = TestData.usd(100)),
            TestData.entry(id = 2, accountId = "acc-1", amount = TestData.eur(100)),
        )

        val error = shouldThrow<IllegalArgumentException> { statement(entries, account) }
        error shouldHaveMessage "mixed currencies in statement"
    }
}
