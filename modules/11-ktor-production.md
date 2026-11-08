# Модуль 11 — Ktor для продакшена

## Цель
Сделать сервис наблюдаемым и защищённым: JWT, health/readiness, Prometheus-метрики, request id, graceful shutdown.

## Предусловия
Модуль 10. `TestJwt` (локальные RSA-ключи) предоставлен; Keycloak не требуется.

## Шпаргалка
`Authentication`/`jwt`, `JWTVerifier`, `authenticate`, `HealthCheck`, `MicrometerMetrics`, `PrometheusMeterRegistry`, JVM binders, `createApplicationPlugin`, `AttributeKey`, `AtomicBoolean`, readiness/drain.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `service/src/main/kotlin/ledger/http/JwtAuth.kt` | `configureJwt`, `protected` | Валидный токен, нет токена, expired, audience, issuer, пустой subject |
| `service/src/main/kotlin/ledger/http/HealthRoutes.kt` | `healthRoutes` | live всегда 200, ready 200/503 |
| `service/src/main/kotlin/ledger/http/Metrics.kt` | `configureMetrics`, `metricsRoute` | JVM-метрики и Ktor-метрики в `/metrics` |
| `service/src/main/kotlin/ledger/http/RequestId.kt` | `installRequestId` | Эхо заголовка, генерация UUID, атрибут и response header |
| `service/src/main/kotlin/ledger/http/ShutdownCoordinator.kt` | `beginShutdown`, `isReady` | Начальное состояние, идемпотентность, потокобезопасность |

## Рефакторинги
1. Добавьте scope-проверку (`transfers:write`) в `validate` и тест на 403.
2. Подключите `JwkProvider` с кэшированием вместо статического public key — какие настройки нужны для ротации?
3. Свяжите `ShutdownCoordinator` с `ApplicationStopping` и readiness-роутом; проверьте, что `/health/ready` возвращает 503 сразу после сигнала.

## Квиз
`quizzes/11-ktor-production.md` — 8 вопросов про JWT, liveness/readiness, кардинальность метрик и graceful shutdown.

## Веха
Защищённый наблюдаемый сервис: `/secure` требует JWT, `/health/ready` отражает состояние, `/metrics` отдаёт Prometheus-формат, каждый ответ содержит `X-Request-Id`.

## Чеклист
- [ ] 11.1 `configureJwt`/`protected` решены.
- [ ] 11.2 `healthRoutes` решён.
- [ ] 11.3 `configureMetrics`/`metricsRoute` решены.
- [ ] 11.4 `installRequestId` решён.
- [ ] 11.5 `ShutdownCoordinator` решён.
- [ ] `./gradlew test -Pmodule=11` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 11` — зелёный.
