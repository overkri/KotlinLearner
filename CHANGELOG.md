# Changelog

Все заметные изменения проекта LedgerKt документируются в этом файле.

Формат основан на [Keep a Changelog](https://keepachangelog.com/ru/1.1.0/),
версионирование — [Semantic Versioning 2.0.0](https://semver.org/lang/ru/).

## [Unreleased]

### Changed

- Gradle wrapper обновлён до **9.8.2**: добавлена поддержка Java 27, сохранена
  совместимость с Java 17–25. Docker build-образ обновлён до `gradle:9.8-jdk21`.
- README уточняет разницу между Gradle JVM и Kotlin toolchain 21.

## [1.0.0] — 2026-11-08

Первый стабильный учебный релиз курса «Kotlin для бэкенд-разработки» и проекта
LedgerKt.

### Added

- **15 модулей курса** (`00`–`14`): от `val` и Java interop до Ktor, Exposed,
  Redis, Kafka и Spring Boot 4.
- **65 упражнений** (121 `TODO`-реализация) в 65 файлах эталонных решений в
  `solutions/` с зеркальной структурой основных модулей.
- **289 тестов** на JUnit Jupiter 6, kotlin-test, MockK, Kotest assertions,
  kotlinx-coroutines-test и Testcontainers 2.
- **Доменный срез LedgerKt**: счета, `Money`, двойная запись, переводы,
  идемпотентность, outbox и события `TransferCompleted`/`TransferRejected`.
- **Ktor-сервис**: routing, DTO, `StatusPages`, JWT, health/live/ready, Micrometer,
  request id и graceful shutdown.
- **PostgreSQL-слой**: HikariCP, Flyway, JDBC-репозиторий с optimistic locking,
  Exposed DSL, транзакции и устранение N+1.
- **Redis и Kafka**: Lettuce coroutines API, идемпотентное хранилище, outbox
  publisher, Kafka producer/consumer с at-least-once доставкой.
- **Spring Boot 4 bridge**: JPA-срез, `@Transactional`, REST controller,
  `@RestControllerAdvice`, MockMvc-тесты.
- **Migration-kata**: перенос `LegacyPricingEngine` на Kotlin, `@JvmOverloads`,
  `@Throws` и review kata по идемпотентности.
- **Документация**: `docs/NN-*.md`, Java→Kotlin cheat sheet, `COVERAGE.md`,
  `99-next-steps.md`, `modules/NN-*.md` и `quizzes/NN-*.md`.
- **Скрипты проверки**: `test-module`, `verify-solutions` и `progress` для bash
  и PowerShell. `verify-solutions` выполняет тесты с `--rerun-tasks
  --no-build-cache`, чтобы gate не мог отчитаться зелёным из Gradle build cache.
- **Инфраструктура**: `compose.yaml` (PostgreSQL 17, Redis 7.4, Kafka KRaft) и
  multi-stage `Dockerfile` для сервиса.
- **Документы релиза**: обновлённый `README.md`, `CHANGELOG.md` и `LICENSE` (MIT).

### Known limitations

- Эталонный `TransferEngine` сохраняет два счёта двумя независимыми вызовами
  `save`; транзакционная атомарность обсуждается в модуле 12, но не реализована
  в in-memory сценарии.
- `IdempotentTransferService` использует неатомарную схему check-then-act;
  конкурентный сценарий и защита от повторного выполнения требуют отдельного
  решения.
- `InMemoryLedger` и in-memory репозитории не потокобезопасны и предназначены
  для демонстрации, а не для production-нагрузки.
- JWT-модуль принимает готовый `JWTVerifier`; проверка issuer/audience/JWKS
  остаётся ответственностью вызывающего кода.

[Unreleased]: https://github.com/overkri/KotlinLearner/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/overkri/KotlinLearner/releases/tag/v1.0.0
