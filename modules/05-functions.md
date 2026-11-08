# Модуль 05 — Функции, расширения, DSL

## Цель
Освоить extension-функции и операторы, scope-функции, типобезопасный DSL и делегаты свойств на примере `Money` и `TransferRequest`.

## Предусловия
Модули 03–04. `Money`, `Transfer`, `TransferRequest`, `Account` готовы.

## Шпаргалка
Extension functions; `operator`; `let/run/with/apply/also`; receiver-лямбды; `@DslMarker`; `Delegates.vetoable`/`observable`; `by lazy`.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/functional/MoneyOperators.kt` | `plus`, `minus`, `times`, `compareTo`, `Iterable<Money>.total()` | Арифметика, mixed currency, отрицательный результат, overflow, пустой total |
| `core/src/main/kotlin/ledger/functional/TransferRequestDsl.kt` | `build()` и `transferRequest { }` | Полная заявка, optional note, обязательные поля |
| `core/src/main/kotlin/ledger/functional/ScopeFunctions.kt` | `auditLine`, `requireActive`, `describeOrNull` | Формат, `takeIf`, `?.let` |
| `core/src/main/kotlin/ledger/functional/TransferLimits.kt` | `Delegates.vetoable` + `allows` | Диапазон 1..1_000_000, игнорирование невалидных значений |

## Рефакторинги
1. Замените `with` в `auditLine` на `transfer.let { ... }` — стало ли лучше?
2. Добавьте в DSL вложенный блок `fee { }` с `@DslMarker` и проверьте, что внешние поля не «протекают».
3. Замените `vetoable` на сеттер с `require` — какое поведение изменится для вызывающего кода?

## Квиз
`quizzes/05-functions.md` — 8 вопросов про `apply/also/let`, `@DslMarker`, `vetoable` и операторы.

## Веха
`transferRequest { ... }` собирает заявку, `Money + Money` работает как оператор, `TransferLimits` отклоняет невалидные лимиты.

## Чеклист
- [ ] 5.1 `MoneyOperators` решён.
- [ ] 5.2 `TransferRequestDsl` решён.
- [ ] 5.3 `ScopeFunctions` решён.
- [ ] 5.4 `TransferLimits` решён.
- [ ] `./gradlew test -Pmodule=05` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 05` — зелёный.
