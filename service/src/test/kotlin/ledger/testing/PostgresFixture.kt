package ledger.testing

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ledger.config.DbConfig
import org.flywaydb.core.Flyway
import org.testcontainers.postgresql.PostgreSQLContainer

/**
 * Один Postgres-контейнер на весь тестовый прогон. Миграции накатываются
 * напрямую через Flyway, чтобы фикстура не зависела от упражнения migrate().
 */
object PostgresFixture {

    private val container: PostgreSQLContainer = PostgreSQLContainer("postgres:17-alpine")
        .withDatabaseName("ledger")
        .withUsername("ledger")
        .withPassword("ledger")
        .apply { start() }

    val config: DbConfig = DbConfig(
        url = container.jdbcUrl,
        user = container.username,
        password = container.password,
        poolSize = 8,
    )

    val dataSource: HikariDataSource by lazy {
        HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = config.url
                username = config.user
                password = config.password
                maximumPoolSize = config.poolSize
                poolName = "ledger-postgres-fixture"
                connectionTimeout = 5_000
            },
        )
    }

    fun migrate() {
        Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .load()
            .migrate()
    }

    fun reset() {
        migrate()
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("TRUNCATE TABLE ledger_entries, accounts RESTART IDENTITY CASCADE")
            }
        }
    }
}
