package ledger.m12

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import ledger.persistence.AccountsTable
import ledger.persistence.TxRunner
import ledger.testing.PostgresFixture
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m12")
@Tag("integration")
class TxRunnerTest {

    private val database = Database.connect(PostgresFixture.dataSource)
    private val runner = TxRunner(database)

    @BeforeEach
    fun reset() {
        PostgresFixture.reset()
    }

    @Test
    fun `commits a successful transaction`() = runTest {
        runner.tx {
            AccountsTable.insert {
                it[id] = "acc-1"
                it[ownerName] = "Alice"
                it[status] = "ACTIVE"
                it[balanceMinor] = 100
                it[currency] = "USD"
                it[version] = 0
            }
        }

        transaction(database) { AccountsTable.selectAll().count() } shouldBe 1
    }

    @Test
    fun `rolls back a failed transaction`() = runTest {
        val error = runCatching {
            runner.tx {
                AccountsTable.insert {
                    it[id] = "acc-1"
                    it[ownerName] = "Alice"
                    it[status] = "ACTIVE"
                    it[balanceMinor] = 100
                    it[currency] = "USD"
                    it[version] = 0
                }
                error("boom")
            }
        }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalStateException>().message shouldBe "boom"
        transaction(database) { AccountsTable.selectAll().count() } shouldBe 0
    }
}
