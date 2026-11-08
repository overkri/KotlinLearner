# 12 — База данных: JDBC, Hikari, Flyway, Exposed

## Цель
Перевести LedgerKt с in-memory на PostgreSQL: пул соединений, миграции, JDBC-репозиторий, Exposed DSL, транзакции и борьба с N+1.

## Предусловия
Модули 03–09. Docker запущен. Подготовлены `DbConfig`, `AccountsTable`, `LedgerEntriesTable`, миграция `V1__init.sql`, Testcontainers-фикстура.

## Шпаргалка
- **HikariCP** — пул соединений; параметры: `jdbcUrl`, `maximumPoolSize`, `connectionTimeout`; закрывайте `HikariDataSource`.
- **Flyway** — версионные миграции `V1__init.sql`; checksum уже применённой миграции менять нельзя.
- **JDBC** — `DataSource.connection.use`, `PreparedStatement`, `ResultSet`; никакой конкатенации значений в SQL.
- **Optimistic locking** — `UPDATE ... WHERE id = ? AND version = ?`; 0 строк = конфликт, а не «данные не найдены».
- **Exposed 1.x** — новые пакеты `org.jetbrains.exposed.v1.core.*`, `v1.jdbc.*`; `transaction(database) { }`, `suspendTransaction(database) { }`; DSL `selectAll().where { id eq ... }`, `insert`, `update`.
- **N+1** — загрузка проводок для N счетов не должна делать N запросов; `WHERE account_id IN (...)` + группировка в память.
- **Testcontainers 2.x** — `org.testcontainers.postgresql.PostgreSQLContainer` (новый пакет), `testcontainers-postgresql` artifact.

## Java → Kotlin
| Java/Spring | Kotlin/Ktor |
|---|---|
| `HikariDataSource` bean | `createDataSource(DbConfig)` |
| `JdbcTemplate` | `DataSource` + `PreparedStatement` |
| `@Transactional` | `suspendTransaction` / `TxRunner.tx` |
| JPA repository | Exposed DSL / ручной JDBC |
| `@Version` | `version` + `UPDATE ... WHERE version = ?` |
| Flyway auto-config | `Flyway.configure().dataSource(...).migrate()` |
| `@DataJpaTest` + Testcontainers | `@Tag("integration")` + singleton `PostgresFixture` |
| `@EntityGraph` / JOIN FETCH | один `IN (...)` + `groupBy` в память |

## Exposed 1.x: важные пакеты
```kotlin
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.javatime.timestamp
```
Если вы ищете примеры Exposed 0.4x, импорты не совпадут: в 1.x всё разложено по `v1`.

## Пять ловушек
1. **Пул больше, чем может Postgres**. `poolSize * pods` может превысить `max_connections`; используйте PgBouncer или меньший пул.
2. **Забыть `WHERE version`**. Два конкурентных перевода прочитают один баланс и потеряют одно списание (lost update).
3. **`UPDATE` 0 строк**. Это может быть optimistic lock, а не «запись отсутствует»; не возвращайте 404 автоматически.
4. **N+1**. На 10 счетах незаметно, на 10 000 — сотни тысяч запросов. Считайте SQL-запросы в тестах (`QueryCounter`).
5. **Flyway checksum**. Изменение уже применённой `V1` ломает старт; добавляйте `V2__...`, а не редактируйте историю.
