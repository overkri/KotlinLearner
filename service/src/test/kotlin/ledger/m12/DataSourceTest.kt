package ledger.m12

import io.kotest.matchers.shouldBe
import ledger.persistence.createDataSource
import ledger.testing.PostgresFixture
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m12")
@Tag("integration")
class DataSourceTest {

    @Test
    fun `creates a working Hikari pool`() {
        createDataSource(PostgresFixture.config.copy(poolSize = 5)).use { dataSource ->
            dataSource.maximumPoolSize shouldBe 5
            dataSource.poolName shouldBe "ledger-kt"
            dataSource.connection.use { connection ->
                connection.isValid(2) shouldBe true
            }
        }
    }
}
