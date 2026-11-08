# Модуль 08 — Flow, каналы, общее состояние

## Цель
Освоить горячие/холодные потоки и конкурентное состояние: `SharedFlow` для событий, `StateFlow` для проекции, `Mutex` для per-account lock, `Channel` для очереди, `Flow.runningFold` для аналитики.

## Предусловия
Модуль 07. `DomainEvent`, `TransferRequest`, `LedgerEntry` готовы; `DomainEvent.TransferCompleted` содержит from/to для проекции.

## Шпаргалка
`Flow`/`SharedFlow`/`StateFlow`, `update`, `Mutex.withLock`, `ConcurrentHashMap.computeIfAbsent`, `Channel`, `runningFold`, `filter`, backpressure, конфляция.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/state/TransferEventBus.kt` | `publish`, `tryPublish`, read-only `events` | Доставка подписчикам, replay, потеря без подписчиков, buffer |
| `core/src/main/kotlin/ledger/state/BalanceProjector.kt` | `apply(event)` и `balances: StateFlow` | Completed меняет балансы, rejected/frozen — нет, missing account = 0 |
| `core/src/main/kotlin/ledger/state/AccountLockRegistry.kt` | `withLock(id)` через per-key `Mutex` | 100 конкурентных инкрементов, сериализация одного счёта, параллельность разных |
| `core/src/main/kotlin/ledger/state/TransferQueue.kt` | `submit`, `start`, `close` | Порядок обработки, дренаж при закрытии, backpressure |
| `core/src/main/kotlin/ledger/state/FlowAnalytics.kt` | `totalsByCurrencyFlow`, `completedOnly` | Running totals, пустой flow, фильтр по статусу, холодность |

## Рефакторинги
1. Замените `SharedFlow` в шине на `Channel` — какие гарантии доставки появились и что потеряли?
2. Сделайте `BalanceProjector` подписчиком `TransferEventBus.events` в отдельной корутине; как обеспечить порядок событий?
3. Ограничьте `TransferQueue` несколькими consumer'ами — как изменится порядок обработки?

## Квиз
`quizzes/08-flow-and-state.md` — 8 вопросов про hot/cold, StateFlow, Mutex, Channel и `update`.

## Веха
Конкурентные переводы не теряют обновления: `AccountLockRegistry` сериализует один счёт, `BalanceProjector` сходится к правильным балансам.

## Чеклист
- [ ] 8.1 `TransferEventBus` решён.
- [ ] 8.2 `BalanceProjector` решён.
- [ ] 8.3 `AccountLockRegistry` решён.
- [ ] 8.4 `TransferQueue` решён.
- [ ] 8.5 `FlowAnalytics` решён.
- [ ] `./gradlew test -Pmodule=08` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 08` — зелёный.
