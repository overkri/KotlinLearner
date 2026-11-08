# PROGRESS — курс «Kotlin для бэкенд-разработки»

Отмечайте упражнение после того, как тест модуля стал зелёным. Решения лежат в `solutions/`; смотреть их рекомендуется только после самостоятельной попытки.

## Модуль 00 — Старт и инструменты
- [ ] 0.1 `FeeCalculatorBridge` — Java interop, platform types, default-аргументы
- [ ] 0.2 `ReceiptPrinter` — constructor injection, `fun interface`, MockK
- [ ] 0.3 `environmentSummary` — top-level-функция, строковые шаблоны, toolchain
- [ ] Веха: проект собирается, первый Java↔Kotlin вызов работает

## Модуль 01 — Синтаксис и выражения
- [ ] 1.1 `formatMinor` — `when`-выражение и строковые шаблоны
- [ ] 1.2 `parseAmount` — nullable-результат и ручной разбор строки
- [ ] 1.3 `feeBps` — диапазоны в `when`, `require`
- [ ] 1.4 `receiptSummary` — циклы, `buildString`, накопление итога
- [ ] Веха: CLI считает и форматирует суммы

## Модуль 02 — Null-safety и Java interop
- [ ] 2.1 `CustomerDirectory` — platform types, `?.`, `?:`, адаптер
- [ ] 2.2 `LazyCustomerIndex` — `by lazy`, однократное построение
- [ ] 2.3 `NullSafetyKata` — `takeIf`, `toIntOrNull`, `requireNotNull`/`error`
- [ ] Веха: безопасная обёртка над Java-справочником

## Модуль 03 — Типы и модель домена
- [ ] 3.1 `Money` — `data class`, `init`, `companion object`
- [ ] 3.2 `Identifiers` — `@JvmInline value class`
- [ ] 3.3 `AccountStatus` — `enum` и переходы
- [ ] 3.4 `Account` — иммутабельность, `copy`, `version`
- [ ] 3.5 `TransferError` — `sealed interface`, `data object`
- [ ] Веха: доменная модель Money/Account/TransferError

## Модуль 04 — Коллекции и Sequence
- [ ] 4.1 `statement` — filter/sorted/running balance
- [ ] 4.2 `totalsByCurrency`, `busiestAccounts`, `averageDebit`
- [ ] 4.3 `firstAbove` — ленивая `Sequence`
- [ ] 4.4 `chunkIds`, `indexById`, `partitionByStatus`
- [ ] Веха: выписка и отчёты по проводкам

## Модуль 05 — Функции, расширения, DSL
- [ ] 5.1 `MoneyOperators` — операторы и `total()`
- [ ] 5.2 `TransferRequestDsl` — `@DslMarker`, `apply`
- [ ] 5.3 `ScopeFunctions` — `let`, `with`, `takeIf`
- [ ] 5.4 `TransferLimits` — `Delegates.vetoable`
- [ ] Веха: `transferRequest { }` и `Money + Money`

## Модуль 06 — Ошибки и Result
- [ ] 6.1 `TransferEngine` — sealed-ошибки и two-phase save
- [ ] 6.2 `withdraw`/`parseAndWithdraw` — `Result`
- [ ] 6.3 `withLedgerBatch` — `use`
- [ ] Веха: CLI-сценарий переводов

## Модуль 07 — Корутины
- [ ] 7.1 `retry` — экспоненциальная задержка
- [ ] 7.2 `TransferGateway` — временные сбои vs бизнес-отказы
- [ ] 7.3 `loadAccounts` — `async`/`awaitAll`
- [ ] 7.4 `withTimeoutFallback` — таймауты
- [ ] 7.5 `runAllSettled` — `supervisorScope`
- [ ] Веха: параллельные вызовы с ретраями

## Модуль 08 — Flow, каналы, общее состояние
- [ ] 8.1 `TransferEventBus` — `SharedFlow`/replay
- [ ] 8.2 `BalanceProjector` — `StateFlow.update`
- [ ] 8.3 `AccountLockRegistry` — per-key `Mutex`
- [ ] 8.4 `TransferQueue` — `Channel` и backpressure
- [ ] 8.5 `FlowAnalytics` — `runningFold`
- [ ] Веха: конкурентные переводы без гонок

## Модуль 09 — Сериализация и конфигурация
- [ ] 9.1 `MoneyAsStringSerializer` — `KSerializer`
- [ ] 9.2 `TransferDto` — DTO ↔ domain, коды ошибок
- [ ] 9.3 `ledgerJson` — настройки `Json`
- [ ] 9.4 `loadConfig` — типизированный конфиг из env
- [ ] Веха: JSON-контракты и конфиг из env

## Модуль 10 — Голый HTTP → Ktor
- [ ] 10.1 `MiniRouter` — routing вручную
- [ ] 10.2 `toHttpStatus` — доменные ошибки → HTTP
- [ ] 10.3 `ledgerModule` — Ktor API
- [ ] Веха: REST API в памяти

## Модуль 11 — Ktor для продакшена
- [ ] 11.1 `configureJwt`/`protected` — JWT
- [ ] 11.2 `healthRoutes` — live/ready
- [ ] 11.3 `configureMetrics`/`metricsRoute` — Prometheus
- [ ] 11.4 `installRequestId` — request id
- [ ] 11.5 `ShutdownCoordinator` — graceful shutdown
- [ ] Веха: защищённый наблюдаемый сервис

## Модуль 12 — База данных
- [ ] 12.1 `createDataSource` — HikariCP
- [ ] 12.2 `JdbcAccountRepository` — JDBC + optimistic lock
- [ ] 12.3 `ExposedAccountRepository` — Exposed DSL
- [ ] 12.4 `TxRunner` — транзакции
- [ ] 12.5 `migrate` — Flyway
- [ ] 12.6 `findByAccountIds` — N+1
- [ ] Веха: сервис на Postgres
