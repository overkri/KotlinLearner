# Модуль 12 — База данных

## Цель
Перевести LedgerKt на PostgreSQL: HikariCP, Flyway, JDBC- и Exposed-репозитории, транзакции, optimistic locking и устранение N+1.

## Предусловия
Модули 03–09. Docker запущен. `DbConfig`, `AccountsTable`, `LedgerEntriesTable`, `V1__init.sql`, `PostgresFixture`, `QueryCounter` предоставлены.

## Шпаргалка
HikariCP; Flyway; PreparedStatement; optimistic locking (`version`); Exposed 1.x (`v1.core`/`v1.jdbc`); `transaction`/`suspendTransaction`; `IN (...)`; Testcontainers 2 (`org.testcontainers.postgresql`).

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `service/src/main/kotlin/ledger/persistence/DataSource.kt` | `createDataSource(DbConfig)` | Пул, `maximumPoolSize`, `poolName`, рабочее соединение |
| `service/src/main/kotlin/ledger/persistence/JdbcAccountRepository.kt` | `findById`, `save` на JDBC | Insert/update, маппинг, optimistic lock |
| `service/src/main/kotlin/ledger/persistence/ExposedAccountRepository.kt` | То же на Exposed DSL | Insert/update, маппинг, optimistic lock |
| `service/src/main/kotlin/ledger/persistence/TxRunner.kt` | `suspendTransaction`-обёртка | Commit, rollback при исключении |
| `service/src/main/kotlin/ledger/persistence/Migrations.kt` | `migrate(dataSource)` | Первый вызов 1, повторный 0, таблица создана |
| `service/src/main/kotlin/ledger/persistence/LedgerEntryRepository.kt` | `findByAccountIds` одним запросом | Группировка, пустые счета, сортировка, ровно 1 запрос |

## Рефакторинги
1. Замените JDBC-репозиторий на Exposed и сравните читаемость и контроль SQL.
2. Добавьте `SELECT ... FOR UPDATE` для перевода и сравните с optimistic locking по конкурентности.
3. Разбейте `findByAccountIds` на `chunked(1000)` для очень больших `IN`-списков.

## Квиз
`quizzes/12-database.md` — 8 вопросов про пул, optimistic locking, Flyway, Exposed 1.x, N+1 и Testcontainers.

## Веха
Сервис работает на Postgres: миграции накатываются, счета сохраняются и обновляются с версией, выписка загружается одним запросом.

## Чеклист
- [ ] 12.1 `createDataSource` решён.
- [ ] 12.2 `JdbcAccountRepository` решён.
- [ ] 12.3 `ExposedAccountRepository` решён.
- [ ] 12.4 `TxRunner` решён.
- [ ] 12.5 `migrate` решён.
- [ ] 12.6 `findByAccountIds` решён.
- [ ] `./gradlew test -Pmodule=12` зелёный (Docker).
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 12` — зелёный (Docker).
