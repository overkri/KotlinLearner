# LedgerKt — курс «Kotlin для бэкенд-разработки»

Учебный проект-тренажёр для Java-бэкендера (6+ лет, Spring Boot/JPA/Kafka/Redis/Postgres), который переходит на Kotlin. Один сквозной FinTech-сервис — счета, двойная запись, переводы, идемпотентность, outbox и Kafka — растёт от `val` и `data class` до Ktor, Exposed, Redis и Spring Boot 4.

Курс рассчитан на **≈80 часов** (8 недель × 10 часов), но проходится в своём темпе: 15 модулей, у каждого — спецификация, шпаргалка, упражнения, тесты, квиз и gate.

## Что внутри
- **15 модулей** (`modules/00-setup.md` … `modules/14-spring-bridge.md`) с упражнениями и критериями готовности.
- **Эталонные решения** в `solutions/` с зеркальной структурой; проверяются отдельным скриптом.
- **Тесты** JUnit 5/6 + kotlin-test + MockK + Kotest assertions + Testcontainers 2.
- **Документация**: `docs/NN-*.md`, единая шпаргалка Java→Kotlin, `docs/99-next-steps.md`, матрица покрытия `docs/COVERAGE.md`.
- **Квизы** `quizzes/NN-*.md` с ответами под `<details>`.
- **Скрипты**: запуск тестов модуля, проверка решений, прогресс.

## Версии (сверены с Maven Central на ноябрь 2026)
| Компонент | Версия |
|---|---|
| Kotlin / Gradle | 2.4.21 / 9.5.1 |
| JDK toolchain | 21 (Eclipse Temurin, auto-provision через Foojay) |
| Ktor | 3.6.1 |
| Exposed | 1.6.1 (`org.jetbrains.exposed.v1.*`) |
| Spring Boot | 4.1.2 (Spring Framework 7, Hibernate 7.4, Jackson 3) |
| Testcontainers | 2.0.5 (новые artifact names: `testcontainers-postgresql`, `testcontainers-kafka`, …) |
| JUnit Jupiter | 6.0.3 |
| kotlinx.coroutines / serialization | 1.11.0 / 1.12.0 |
| PostgreSQL / Redis / Kafka | 17 / 7.4 / Kafka 4.x (KRaft) |

Отклонения от исходного плана записаны в `plan.md` §7.3.

## Быстрый старт
```bash
# Сборка без тестов (заглушки компилируются)
./gradlew build -x test

# Инфраструктура для ручного запуска и интеграционных тестов
docker compose up -d

# Тесты одного модуля (NN = 00..14)
./gradlew test -Pmodule=00

# Проверка эталонных решений модуля (копия проекта + overlay solutions/)
./scripts/verify-solutions.sh 00

# Проверка всех решений (включая интеграционные тесты)
./scripts/verify-solutions.sh
```
Windows PowerShell:
```powershell
.\gradlew.bat build -x test
.\gradlew.bat test -Pmodule=00
.\scripts\verify-solutions.ps1 -Module 00
.\scripts\verify-solutions.ps1
```

> JDK 21 не обязателен локально: Gradle provisioning скачает Temurin 21. Docker нужен для модулей 12–13 и для `verify-solutions` без `-Module` (интеграционные тесты).

## Как проходить модуль
1. Прочитайте `modules/NN-*.md` и `docs/NN-*.md`.
2. Откройте файлы упражнений из таблицы в спецификации.
3. Замените `TODO("mNN.x.y")` на реализацию. Сигнатуры, KDoc и data-контракты менять нельзя.
4. Запускайте `./gradlew test -Pmodule=NN` до зелёного.
5. Пройдите квиз `quizzes/NN-*.md`.
6. Только после попытки смотрите `solutions/` и сравнивайте подходы.
7. Отметьте пункты в `PROGRESS.md` и переходите к следующему модулю.

Подсказки в KDoc раскрываются по одной. Имена тестов — предложения (`rejects transfer to the same account`), сообщения assert'ов подсказывают, что именно не так.

## Программа
| № | Модуль | Темы | Часы |
|---|---|---|---|
| 00 | [Старт и инструменты](modules/00-setup.md) | Gradle KTS, JUnit/MockK, K2, Java↔Kotlin | 2 |
| 01 | [Синтаксис и выражения](modules/01-syntax.md) | `val/var`, `when`, циклы, строки | 3 |
| 02 | [Null-safety и Java interop](modules/02-null-safety.md) | platform types, `?:`, `lazy` | 3 |
| 03 | [Типы и модель домена](modules/03-types.md) | data/value/sealed/enum/object | 5 |
| 04 | [Коллекции и Sequence](modules/04-collections.md) | map/filter/fold/groupBy, laziness | 5 |
| 05 | [Функции, расширения, DSL](modules/05-functions.md) | extensions, операторы, scope, делегаты | 6 |
| 06 | [Ошибки и Result](modules/06-errors.md) | sealed-ошибки, `Result`, `use` | 4 |
| 07 | [Корутины](modules/07-coroutines.md) | suspend, structured concurrency, retry | 7 |
| 08 | [Flow, каналы, состояние](modules/08-flow-and-state.md) | SharedFlow, StateFlow, Mutex, Channel | 5 |
| 09 | [Сериализация и конфигурация](modules/09-serialization-config.md) | kotlinx.serialization, DTO, env | 4 |
| 10 | [Голый HTTP → Ktor](modules/10-http-ktor.md) | routing, StatusPages, testApplication | 6 |
| 11 | [Ktor для продакшена](modules/11-ktor-production.md) | JWT, health, метрики, shutdown | 7 |
| 12 | [База данных](modules/12-database.md) | JDBC, Hikari, Flyway, Exposed, N+1 | 8 |
| 13 | [Redis, Kafka, outbox](modules/13-redis-kafka.md) | Lettuce, идемпотентность, outbox | 8 |
| 14 | [Spring Boot + Kotlin](modules/14-spring-bridge.md) | JPA-срез, ревью, миграция | 6 |
| | **Итого** | | **≈79** |

## Структура
```
core/            чистый Kotlin: синтаксис, типы, коллекции, корутины, сериализация
service/         Ktor-сервис, Postgres/Exposed, Redis, Kafka, outbox
spring-bridge/   Spring Boot 4 + Kotlin, JPA, MockMvc
migration-kata/  Java→Kotlin миграция, review kata, @JvmOverloads/@Throws
solutions/       эталонные решения с зеркальной структурой
docs/            шпаргалки, cheatsheet, COVERAGE, next steps
modules/         спецификации модулей 00–14
quizzes/         вопросы с ответами
scripts/         test-module, verify-solutions, progress
```

## Проверки и прогресс
```bash
# Прогресс по JUnit XML (после прогонов)
./scripts/progress.sh          # bash
.\scripts\progress.ps1         # PowerShell
```
Каждый модуль имеет gate: `build -x test` зелёный, тесты на заглушках падают на `NotImplementedError`, `verify-solutions` зелёный. Полная проверка решений запускает и интеграционные тесты (Docker).

## Docker и порты
`compose.yaml` поднимает:
- PostgreSQL 17 — `localhost:5432` (`ledger/ledger`, БД `ledger`);
- Redis 7.4 — `localhost:6379`;
- Kafka (KRaft) — `localhost:9092`.

Testcontainers использует собственные контейнеры (`postgres:17-alpine`, `redis:7.4-alpine`, `apache/kafka-native:4.0.0`) и не конфликтует с compose, если порты не проброшены наружу.

## FAQ
**Docker не запущен, интеграционные тесты падают.** Запустите Docker Desktop и проверьте `docker run --rm hello-world`. Модули 00–11 работают без Docker; 12–13 требуют Docker.

**Порты заняты.** Остановите локальные Postgres/Redis/Kafka или измените порты в `compose.yaml`.

**Windows/WSL и Testcontainers.** Включите интеграцию Docker Desktop с WSL. PowerShell-скрипты — `.\scripts\...ps1`; bash-скрипты — Git Bash/WSL/macOS/Linux.

**Почему `build -x test` зелёный, а тесты падают?** Заглушки компилируются, но бросают `NotImplementedError` — это точка старта упражнения.

**Где решения?** В `solutions/`. Они не подключены к сборке студента: `scripts/verify-solutions.*` копирует проект во временную папку и накладывает решения сверху.

**Spring Boot 4 и старые примеры.** В Boot 4 Jackson 3 (`tools.jackson`), Hibernate 7, Jakarta EE 11, тестовые аннотации переехали в `org.springframework.boot.webmvc.test.autoconfigure`. Примеры для Boot 3 могут не компилироваться.

**Kotlin 2.4 и Spring Boot 4.1.** Boot 4.1 собран с Kotlin 2.3; проект использует 2.4.21. Совместимость проверяется сборкой; при проблемах зафиксируйте Kotlin 2.3.x и запишите отклонение.

## Лицензия
Учебный материал; используйте свободно для обучения.
