# Модуль 07 — Корутины

## Цель
Освоить `suspend`, structured concurrency, ретраи, таймауты и отмену на примере перевода денег и загрузки счетов.

## Предусловия
Модули 00–06. `TransferClient`, `RetryPolicy`, `AccountRepository` предоставлены.

## Шпаргалка
`suspend`, `launch`/`async`, `coroutineScope`/`supervisorScope`, `delay`, `withTimeoutOrNull`, `CancellationException`, `runTest` + виртуальное время, `Dispatchers.IO`.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/coroutines/Retry.kt` | `retry` с экспоненциальной задержкой | Успех, 3 попытки, виртуальное время 300, non-retryable, параметры |
| `core/src/main/kotlin/ledger/coroutines/TransferGateway.kt` | `transfer` через `retry` | Retryable повторяется, business failure — нет, non-retryable пробрасывается |
| `core/src/main/kotlin/ledger/coroutines/ParallelAggregator.kt` | `loadAccounts` через `async/awaitAll` | Параллельность (время 1000), порядок, отмена siblings |
| `core/src/main/kotlin/ledger/coroutines/Timeout.kt` | `withTimeoutFallback` | Таймаут -> fallback, быстрый успех, проброс ошибки |
| `core/src/main/kotlin/ledger/coroutines/StructuredConcurrency.kt` | `runAllSettled` через `supervisorScope` | Все задачи выполняются, ошибки изолированы, `CancellationException` не глотается |

## Рефакторинги
1. Замените `runAllSettled` на `list.map { runCatching { it() } }` и объясните, что сломается с отменой.
2. Добавьте в `retry` jitter (случайную добавку к задержке) — как тестировать детерминированно?
3. Ограничьте параллелизм `loadAccounts` через `Semaphore(4)` и сравните время выполнения.

## Квиз
`quizzes/07-coroutines.md` — 8 вопросов про structured concurrency, отмену, `delay`, таймауты и диспетчеры.

## Веха
Параллельная загрузка счетов с ретраями: временные сбои повторяются, бизнес-отказы не повторяются, таймаут возвращает fallback.

## Чеклист
- [ ] 7.1 `retry` решён.
- [ ] 7.2 `TransferGateway` решён.
- [ ] 7.3 `loadAccounts` решён.
- [ ] 7.4 `withTimeoutFallback` решён.
- [ ] 7.5 `runAllSettled` решён.
- [ ] `./gradlew test -Pmodule=07` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 07` — зелёный.
