package ledger.persistence

import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun migrate(dataSource: DataSource): Int =
    Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration")
        .load()
        .migrate()
        .migrationsExecuted
