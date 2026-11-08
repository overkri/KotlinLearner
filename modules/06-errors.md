# Модуль 06 — Ошибки и Result

## Цель
Реализовать чистый движок переводов, который возвращает sealed-ошибки вместо исключений, освоить `Result` и `use`, понять границу транзакции.

## Предусловия
Модули 03–05. `TransferRequest`, `TransferResult`, `TransferError`, `AccountRepository`, `InMemoryAccountRepository` предоставлены.

## Шпаргалка
Исключения vs значения; sealed-ошибки; `Result.success/failure/map/fold/getOrElse`; `runCatching` и `CancellationException`; `use`; отсутствие checked exceptions; `@Throws` для Java.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/errors/TransferEngine.kt` | `transfer(request)` со всеми проверками и сохранением двух счетов | Успех, missing/frozen/currency/insufficient/same/invalid, отсутствие частичных save |
| `core/src/main/kotlin/ledger/errors/ResultService.kt` | `withdraw`, `parseAndWithdraw` через `Result` | Успех, zero, frozen, mixed currency, insufficient, invalid string |
| `core/src/main/kotlin/ledger/errors/Use.kt` | `withLedgerBatch` через `use` | Возврат значения, закрытие при успехе и исключении |

## Рефакторинги
1. Перепишите `TransferEngine` на `Result<Transfer>` вместо `TransferResult` — какие ошибки стали неудобны?
2. Добавьте в `withdraw` `mapCatching` для парсинга и сравните с явной проверкой.
3. Оберните оба `save` в гипотетический `transaction { }` (пока заглушкой) и объясните, что изменится при падении второго save.

## Квиз
`quizzes/06-errors.md` — 8 вопросов про checked exceptions, `runCatching`, `Result`, `use` и транзакции.

## Веха
CLI-сценарий переводов: успешный перевод меняет балансы, а бизнес-ошибки возвращаются как `TransferResult.Failure` без исключений.

## Чеклист
- [ ] 6.1 `TransferEngine` решён.
- [ ] 6.2 `ResultService` решён.
- [ ] 6.3 `withLedgerBatch` решён.
- [ ] `./gradlew test -Pmodule=06` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 06` — зелёный.
