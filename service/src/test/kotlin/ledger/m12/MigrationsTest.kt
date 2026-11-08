package ledger.m12

import io.kotest.matchers.shouldBe
import ledger.persistence.migrate
import ledger.testing.PostgresFixture
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m12")
@Tag("integration")
class MigrationsTest {

    @Test
    fun `applies migrations once and is idempotent`() {
        PostgresFixture.dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("DROP TABLE IF EXISTS ledger_entries, accounts CASCADE")
                statement.execute("DROP TABLE IF EXISTS flyway_schema_history CASCADE")
            }
        }

        migrate(PostgresFixture.dataSource) shouldBe 1
        migrate(PostgresFixture.dataSource) shouldBe 0

        PostgresFixture.dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(
                    "SELECT to_regclass('public.accounts') AS table_name",
                ).use { result ->
                    result.next() shouldBe true
                    result.getString("table_name") shouldBe "accounts"
                }
            }
        }
    }
}
