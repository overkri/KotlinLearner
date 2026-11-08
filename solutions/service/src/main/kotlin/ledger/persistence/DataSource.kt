package ledger.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ledger.config.DbConfig

fun createDataSource(config: DbConfig): HikariDataSource {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = config.url
        username = config.user
        password = config.password
        maximumPoolSize = config.poolSize
        poolName = "ledger-kt"
        connectionTimeout = 3_000
    }
    return HikariDataSource(hikariConfig)
}
