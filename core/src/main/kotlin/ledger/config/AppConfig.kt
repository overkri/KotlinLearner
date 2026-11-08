package ledger.config

/**
 * Типизированная конфигурация приложения. Data-only контракты предоставлены;
 * студент реализует только загрузчик.
 */
data class HttpConfig(
    val host: String,
    val port: Int,
)

data class DbConfig(
    val url: String,
    val user: String,
    val password: String,
    val poolSize: Int,
)

data class JwtConfig(
    val issuer: String,
    val audience: String,
    val jwksUrl: String,
)

data class AppConfig(
    val http: HttpConfig,
    val db: DbConfig,
    val jwt: JwtConfig,
)
