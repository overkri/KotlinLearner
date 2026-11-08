# COVERAGE — темы §1.3 → упражнения

| Тема | Где встречается |
|---|---|
| null-safety, `?`, `?:` | 2.1 `CustomerDirectory`, 2.3 `NullSafetyKata` |
| platform types Java→Kotlin | 0.1 `FeeCalculatorBridge`, 2.1 `CustomerDirectory` |
| `final` по умолчанию / `open` | 14.1 `SpringAccountService` + `kotlin-spring` (allopen) |
| свойства вместо getter/setter | 3.4 `Account`, 9.4 `AppConfig`, весь домен |
| `data class` | 3.1 `Money`, 3.4 `Account`, DTO в 9 |
| `value class` | 3.2 `AccountId`, `TransferId`, `IdempotencyKey` |
| `sealed` | 3.5 `TransferError`, 6.1 `TransferEngine`, 8.2 `BalanceProjector` |
| `enum` | 3.3 `AccountStatus`, 3.1 `Currency` |
| `object` / `companion object` | 0.3 `environmentSummary`, 3.1 `Money.Companion` |
| выражения (`if`/`when`/`try`) | 1.1 `formatMinor`, 1.3 `feeBps`, 6.1 `TransferEngine` |
| default- и named-аргументы | 0.1 `FeeCalculatorBridge`, 5.2 DSL |
| extension-функции | 5.1 `MoneyOperators`, 5.3 `ScopeFunctions` |
| лямбды и scope-функции | 5.3 `ScopeFunctions`, 4.2 `Analytics` |
| операторы | 5.1 `MoneyOperators` |
| DSL | 5.2 `TransferRequestDsl` |
| делегаты свойств | 5.4 `TransferLimits` (`Delegates.vetoable`) |
| read-only vs mutable коллекции | 4.1 `statement`, 4.4 `BatchIndex` |
| `Sequence` | 4.3 `firstAbove` (тест на laziness) |
| нет checked exceptions | 6.2 `ResultService`, docs 06 |
| `Result` | 6.2 `withdraw`/`parseAndWithdraw` |
| `use` | 6.3 `withLedgerBatch` |
| корутины, `suspend` | 7.1 `retry`, 7.2 `TransferGateway` |
| structured concurrency | 7.3 `loadAccounts`, 7.5 `runAllSettled` |
| отмена/таймауты | 7.4 `withTimeoutFallback` |
| `Flow` | 8.5 `FlowAnalytics` |
| `Channel` | 8.4 `TransferQueue` |
| `Mutex` | 8.3 `AccountLockRegistry` |
| `SharedFlow`/`StateFlow` | 8.1 `TransferEventBus`, 8.2 `BalanceProjector` |
| interop-аннотации (`@Jvm*`, `@Throws`) | 14.6 `JvmAnnotationsKata` + Java-тест |
| `kotlin-spring` (allopen) | 14.1/14.2 Spring-бины |
| `kotlin-jpa` (noarg) | 14.0 `AccountEntity` + конфигурация плагина |
| Context parameters / explicit backing fields | только docs (сознательно без упражнений, §1.4) |

## Дополнительно покрыто
- JSON: `kotlinx.serialization`, кастомный `KSerializer`, DTO ↔ домен (модуль 09).
- HTTP: routing, StatusPages, `testApplication` (модуль 10).
- Production: JWT, health, Micrometer, request id, shutdown (модуль 11).
- SQL: JDBC, Hikari, Flyway, Exposed, optimistic locking, N+1 (модуль 12).
- Распределённые гарантии: Redis idempotency, outbox, Kafka at-least-once (модуль 13).
- Spring Boot 4: JPA-срез, MockMvc, exception handler, configuration properties (модуль 14).
