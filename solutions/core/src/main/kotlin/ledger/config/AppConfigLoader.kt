package ledger.config

fun loadConfig(env: Map<String, String>): AppConfig {
    fun value(key: String, default: String): String =
        env[key]?.trim()?.takeIf { it.isNotEmpty() } ?: default

    fun required(key: String): String =
        env[key]?.trim()?.takeIf { it.isNotEmpty() }
            ?: throw IllegalArgumentException("$key is required")

    val port = value("HTTP_PORT", "8080")
        .toIntOrNull()
        ?.takeIf { it in 1..65535 }
        ?: throw IllegalArgumentException("HTTP_PORT must be a valid port")

    val poolSize = value("DB_POOL_SIZE", "10")
        .toIntOrNull()
        ?.takeIf { it in 1..100 }
        ?: throw IllegalArgumentException("DB_POOL_SIZE must be a valid pool size")

    return AppConfig(
        http = HttpConfig(
            host = value("HTTP_HOST", "0.0.0.0"),
            port = port,
        ),
        db = DbConfig(
            url = required("DB_URL"),
            user = value("DB_USER", "ledger"),
            password = value("DB_PASSWORD", "ledger"),
            poolSize = poolSize,
        ),
        jwt = JwtConfig(
            issuer = required("JWT_ISSUER"),
            audience = required("JWT_AUDIENCE"),
            jwksUrl = required("JWT_JWKS_URL"),
        ),
    )
}
