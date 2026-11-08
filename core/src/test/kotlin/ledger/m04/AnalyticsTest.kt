package ledger.m04

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.collections.averageDebit
import ledger.collections.busiestAccounts
import ledger.collections.totalsByCurrency
import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.Direction
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m04")
class AnalyticsTest {

    private val entries = listOf(
        TestData.entry(id = 1, accountId = "acc-1", direction = Direction.DEBIT, amount = TestData.usd(100)),
        TestData.entry(id = 2, accountId = "acc-1", direction = Direction.CREDIT, amount = TestData.usd(50)),
        TestData.entry(id = 3, accountId = "acc-2", direction = Direction.DEBIT, amount = TestData.eur(30)),
    )

    @Test
    fun `totals are signed per currency`() {
        totalsByCurrency(entries) shouldBe mapOf(
            Currency.USD to -50L,
            Currency.EUR to -30L,
        )
    }

    @Test
    fun `totals of an empty list are empty`() {
        totalsByCurrency(emptyList()) shouldBe emptyMap()
    }

    @Test
    fun `busiest accounts are sorted by count and then id`() {
        val busy = entries + listOf(
            TestData.entry(id = 4, accountId = "acc-2", direction = Direction.CREDIT, amount = TestData.eur(1)),
            TestData.entry(id = 5, accountId = "acc-3", direction = Direction.CREDIT, amount = TestData.eur(1)),
        )

        busiestAccounts(busy, 2) shouldBe listOf(
            AccountId("acc-1") to 2,
            AccountId("acc-2") to 2,
        )
    }

    @Test
    fun `negative limit is rejected`() {
        val error = shouldThrow<IllegalArgumentException> { busiestAccounts(entries, -1) }
        error shouldHaveMessage "limit must be non-negative"
    }

    @Test
    fun `average debit uses integer division`() {
        val debits = listOf(
            TestData.entry(id = 1, direction = Direction.DEBIT, amount = TestData.usd(100)),
            TestData.entry(id = 2, direction = Direction.DEBIT, amount = TestData.usd(51)),
        )

        averageDebit(debits) shouldBe TestData.usd(75)
    }

    @Test
    fun `average debit returns null without debits`() {
        val credits = listOf(TestData.entry(direction = Direction.CREDIT))

        averageDebit(credits) shouldBe null
        averageDebit(emptyList()) shouldBe null
    }

    @Test
    fun `average debit rejects mixed currencies`() {
        val debits = listOf(
            TestData.entry(id = 1, direction = Direction.DEBIT, amount = TestData.usd(100)),
            TestData.entry(id = 2, direction = Direction.DEBIT, amount = TestData.eur(100)),
        )

        val error = shouldThrow<IllegalArgumentException> { averageDebit(debits) }
        error shouldHaveMessage "mixed currencies"
    }
}
