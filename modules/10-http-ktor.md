# Модуль 10 — Голый HTTP → Ktor

## Цель
Собрать REST API LedgerKt на Ktor: routing, path-параметры, DTO, StatusPages, `testApplication`, маппинг доменных ошибок в HTTP.

## Предусловия
Модули 03–09. `LedgerDependencies`, `AccountDto`, `TransferRequestDto/TransferResponseDto/ErrorDto` предоставлены.

## Шпаргалка
`routing`, `get/post`, `call.parameters`, `call.receive`, `call.respond`, `ContentNegotiation`, `StatusPages`, `testApplication`, HTTP-коды, DTO-граница.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `service/src/main/kotlin/ledger/http/MiniRouter.kt` | `register`, `route` с path-параметрами | Статические/параметризованные маршруты, метод, лишние сегменты, порядок |
| `service/src/main/kotlin/ledger/http/ErrorMapping.kt` | `TransferError.toHttpStatus()` | 400/404/409/422 для всех ошибок |
| `service/src/main/kotlin/ledger/http/LedgerApplication.kt` | `Application.ledgerModule` | health, accounts, transfer success/error, invalid DTO, malformed JSON, 404 |

## Рефакторинги
1. Вынесите обработчики в отдельные функции `Route.accountRoutes(deps)` и `Route.transferRoutes(deps)`.
2. Замените `MiniRouter` на Ktor routing в тесте и сравните количество кода.
3. Добавьте `GET /transfers/{id}` — какие слои нужно расширить (service, repository, DTO)?

## Квиз
`quizzes/10-http-ktor.md` — 8 вопросов про routing, ContentNegotiation, StatusPages, коды и testApplication.

## Веха
`./gradlew :service:run` поднимает сервис; `curl /health`, `/accounts`, `POST /transfers` работают на in-memory данных.

## Чеклист
- [ ] 10.1 `MiniRouter` решён.
- [ ] 10.2 `toHttpStatus` решён.
- [ ] 10.3 `ledgerModule` решён.
- [ ] `./gradlew test -Pmodule=10` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 10` — зелёный.
