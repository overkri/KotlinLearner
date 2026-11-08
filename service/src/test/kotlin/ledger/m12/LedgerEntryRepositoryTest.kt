package ledger.m12

import io.kotest.matchers.shouldBe
import ledger.domain.AccountId
import ledger.domain.Direction
import ledger.persistence.JdbcLedgerEntryRepository
import ledger.testing.PostgresFixture
import ledger.testing.QueryCounter
import ledger.testing.TestData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.sql.Timestamp

@Tag("m12")
@Tag("integration")
class LedgerEntryRepositoryTest {

    @BeforeEach
    fun reset() {
        PostgresFixture.reset()
    }

    private fun insert(entry: ledger.domain.LedgerEntry) {
        PostgresFixture.dataSource.connection.use { connection ->
            connection.prepareStatement(
                """
                INSERT INTO ledger_entries
                    (id, transfer_id, account_id, counterparty, direction, amount_minor, currency, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
            ).use { statement ->
                statement.setLong(1, entry.id)
                statement.setString(2, entry.transferId.value)
                statement.setString(3, entry.accountId.value)
                statement.setString(4, entry.counterparty.value)
                statement.setString(5, entry.direction.name)
                statement.setLong(6, entry.amount.minor)
                statement.setString(7, entry.amount.currency.name)
                statement.setTimestamp(8, Timestamp.from(entry.createdAt))
                statement.executeUpdate()
            }
        }
    }

    @Test
    fun `loads entries for all requested accounts with one query`() {
        insert(TestData.entry(id = 2, accountId = "a", direction = Direction.CREDIT, amount = TestData.usd(200)))
        insert(TestData.entry(id = 1, accountId = "a", direction = Direction.DEBIT, amount = TestData.usd(100)))
        insert(TestData.entry(id = 3, accountId = "b", direction = Direction.DEBIT, amount = TestData.usd(300)))

        val counter = QueryCounter(PostgresFixture.dataSource)
        val repository = JdbcLedgerEntryRepository(counter)

        val result = repository.findByAccountIds(
            listOf(AccountId("a"), AccountId("b"), AccountId("c")),
        )

        result.keys shouldBe setOf(AccountId("a"), AccountId("b"), AccountId("c"))
        result[AccountId("a")]?.map { it.id } shouldBe listOf(1L, 2L)
        result[AccountId("b")]?.map { it.id } shouldBe listOf(3L)
        result[AccountId("c")] shouldBe emptyList()
        counter.count.get() shouldBe 1
    }

    @Test
    fun `does not query for an empty input`() {
        val counter = QueryCounter(PostgresFixture.dataSource)
        val repository = JdbcLedgerEntryRepository(counter)

        repository.findByAccountIds(emptyList()) shouldBe emptyMap()
        counter.count.get() shouldBe 0
    }
}
