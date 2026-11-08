package ledger.persistence

import com.zaxxer.hikari.HikariDataSource
import ledger.config.DbConfig

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 12 · УПРАЖНЕНИЕ 12.1 — HikariCP
 * ═══════════════════════════════════════════════════════════
 *  Тема:           connection pool, HikariConfig, fail-fast
 *  Java-аналогия:  HikariDataSource bean в Spring Boot
 *  Тесты:          ./gradlew :service:test -Pmodule=12 --tests "ledger.m12.DataSourceTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Создайте пул соединений по DbConfig.
 *
 *  ТРЕБОВАНИЯ
 *  1. jdbcUrl, username, password из DbConfig.
 *  2. maximumPoolSize = config.poolSize.
 *  3. poolName = "ledger-kt".
 *  4. connectionTimeout = 3000 ms.
 *  5. Вернуть HikariDataSource; вызывающий закрывает его.
 *
 *  ПОДСКАЗКИ
 *  - `HikariConfig().apply { jdbcUrl = ... }`.
 *  - `HikariDataSource(config)` создаёт пул и проверяет подключение.
 *  - Не логируйте пароль.
 *
 *  ЛОВУШКА
 *  Что произойдёт, если poolSize больше max_connections Postgres? Почему
 *  пул на каждый pod — плохая идея без PgBouncer?
 * ═══════════════════════════════════════════════════════════
 */
fun createDataSource(config: DbConfig): HikariDataSource = TODO("m12.1.1")
