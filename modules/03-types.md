# Модуль 03 — Типы и модель домена

## Цель
Смоделировать домен LedgerKt иммутабельными типами: `Money`, value-class идентификаторы, `Account` с версионированием, `enum`-статусы и sealed-ошибки.

## Предусловия
Модули 00–02. Data-only контракты `Currency`, `Transfer`, `TransferRequest`, `TransferResult`, `LedgerEntry`, `DomainEvent`, `AccountRepository` уже предоставлены в `ledger.domain`.

## Шпаргалка
`data class` / `value class` / `sealed interface` / `enum` / `object`; `init`-инварианты; `copy`; `companion object`; исчерпывающий `when`; иммутабельность и `version` для optimistic locking.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/domain/Money.kt` | `Money` с init-валидацией, `isZero`, `zero`, `ofMajor` | Отрицательные суммы, JPY, фабрики, data class equality/copy |
| `core/src/main/kotlin/ledger/domain/Identifiers.kt` | `AccountId`, `TransferId`, `IdempotencyKey` как `@JvmInline value class` | Валидация blank/длины/пробелов, равенство по значению |
| `core/src/main/kotlin/ledger/domain/AccountStatus.kt` | `isTerminal`, `canTransitionTo` | Матрица переходов ACTIVE/FROZEN/CLOSED |
| `core/src/main/kotlin/ledger/domain/Account.kt` | Иммутабельный `Account`, переходы статуса, `withBalance`, `version` | Идемпотентность, запрет закрытия с балансом, currency mismatch |
| `core/src/main/kotlin/ledger/domain/TransferError.kt` | Sealed-иерархия ошибок с сообщениями | Тексты, `data object`, исчерпывающий `when` |

## Рефакторинги
1. Замените `data object SameAccount` на `data class SameAccount(val accountId: AccountId)` — какие тесты и обработчики придётся изменить?
2. Сделайте `Money` `value class` — какие ограничения появятся у `init`, `companion` и сериализации?
3. Добавьте в `TransferError` ошибку `DailyLimitExceeded` — убедитесь, что тестовый `when` перестал компилироваться без неё.

## Квиз
`quizzes/03-types.md` — 8 вопросов про data/value/sealed/enum/object, `copy` и инварианты.

## Веха
Доменная модель `Money`/`Account`/`TransferError` готова: суммы валидируются, счёт меняет статус и версию, ошибки обрабатываются исчерпывающим `when`.

## Чеклист
- [ ] 3.1 `Money` решён.
- [ ] 3.2 `Identifiers` решён.
- [ ] 3.3 `AccountStatus` решён.
- [ ] 3.4 `Account` решён.
- [ ] 3.5 `TransferError` решён.
- [ ] `./gradlew test -Pmodule=03` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 03` — зелёный.
