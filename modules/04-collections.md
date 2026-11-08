# Модуль 04 — Коллекции и Sequence

## Цель
Научиться строить отчёты и выписки по проводкам через коллекции и ленивые `Sequence`, не превращая код в циклы с ручными индексами.

## Предусловия
Модуль 03. `LedgerEntry`, `Direction`, `StatementRow` предоставлены.

## Шпаргалка
`filter`, `map`, `sortedWith`, `groupBy`, `groupingBy/eachCount`, `associateBy`, `partition`, `chunked`, `sumOf`, `firstOrNull`, `asSequence`, laziness.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/collections/StatementBuilder.kt` | `statement(entries, accountId)` с фильтрацией, сортировкой, running balance | Порядок, tie-breaker по id, знаковый остаток, mixed currencies |
| `core/src/main/kotlin/ledger/collections/Analytics.kt` | `totalsByCurrency`, `busiestAccounts`, `averageDebit` | Знаковые суммы, сортировка/лимит, integer average, mixed currencies |
| `core/src/main/kotlin/ledger/collections/SequenceReport.kt` | `firstAbove` на `Sequence` | Short-circuit: тест считает обращения к элементам |
| `core/src/main/kotlin/ledger/collections/BatchIndex.kt` | `chunkIds`, `indexById`, `partitionByStatus` | Разбиение, дубликаты, порядок активных/остальных |

## Рефакторинги
1. Перепишите `statement` через `fold` вместо `var balance` — что стало лучше/хуже?
2. Замените `groupBy().mapValues` в `totalsByCurrency` на `groupingBy().fold(0L) { acc, e -> acc + signedMinor(e) }`.
3. Сделайте `firstAbove` через `asSequence().filter(...).firstOrNull()` и сравните с `firstOrNull { ... }` по читаемости.

## Квиз
`quizzes/04-collections.md` — 8 вопросов про laziness, `associateBy`, `groupingBy`, сортировку и read-only-коллекции.

## Веха
Выписка по счёту сортируется по времени, считает остаток после каждой проводки и не смешивает валюты.

## Чеклист
- [ ] 4.1 `statement` решён.
- [ ] 4.2 `totalsByCurrency`/`busiestAccounts`/`averageDebit` решены.
- [ ] 4.3 `firstAbove` ленив (тест на счётчик проходит).
- [ ] 4.4 `chunkIds`/`indexById`/`partitionByStatus` решены.
- [ ] `./gradlew test -Pmodule=04` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 04` — зелёный.
