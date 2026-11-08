# plan.md — Курс «Kotlin для бэкенд-разработки» через проект LedgerKt

> **Для кодинг-агента.** Это индекс-ТЗ. Он описывает допущения, правила, структуру репозитория и порядок работы. Детальные спецификации модулей лежат в `modules/NN-*.md`. Работай **строго по одному модулю за раз** (см. §7).
> Студент: Java-бэкендер 6+ лет (Spring Boot, JPA, Kafka, Redis, Postgres, Docker/K8s). Kotlin не знает. Цель курса — уверенно писать бэкенд-сервисы на Kotlin, понимать, что именно Spring скрывает от разработчика, и читать/ревьюить Kotlin-код коллег (в основном Spring Boot + Kotlin).

---

## 1. Допущения и обоснование выбора

### 1.1. Допущения (приняты вместо вопросов)
1. **Формулировка цели в запросе неоднозначна** («перейти на Kotlin как на аналог Spring»). Трактовка: основной курс использует **Kotlin-нативный фреймворк (Ktor)**, чтобы показать, что Spring делает «за кадром», а отдельный итоговый модуль 14 переводит срез проекта на **Spring Boot 4 + Kotlin** и учит читать чужой код.
2. Темп не указан: **8 недель × ~10 часов в неделю ≈ 80 часов** (оценки по модулям в §3).
3. Версии (на ноябрь 2026): **Kotlin 2.4.0** (стабильный с 3 июня 2026), **JDK 21** (toolchain), **Gradle 9.x** (Kotlin DSL + version catalog), **Ktor 3.6.x**, **Exposed 1.0.x**, **Spring Boot 4.1.x** (Kotlin baseline 2.2, BOM на 2.3). Spring Boot 4.2 ожидался в ноябре 2026 — если на момент генерации он уже вышел, остаёмся на проверенной сборкой 4.1.x и пишем это в «Отклонения».
4. **Отклонение при несовместимости:** если на шаге скелета (модуль 0) сборка `spring-bridge` с Kotlin 2.4.0 не проходит, агент фиксирует Kotlin = последняя стабильная 2.3.x **на весь проект** и записывает это в раздел «Отклонения» внизу этого файла.
5. **Версии и API библиотек агент сверяет по актуальной документации/Maven Central, а не по памяти** (особенно Ktor 3.x, Exposed 1.x — у него новые пакеты `org.jetbrains.exposed.v1.*`, Spring Boot 4, Testcontainers 2.x, Kotlin serialization). Итоговые версии записываются в `gradle/libs.versions.toml`.
6. На машине студента есть Docker (для Testcontainers и compose), IntelliJ IDEA, JDK 21.
7. Тесты на БД/Redis/Kafka используют **Testcontainers** (JVM-стандарт, студент знает его из Java). Compose-файл — только для ручного запуска сервиса.
8. Все комментарии/документация на русском; идентификаторы, имена файлов и сообщения тестов — английские.

### 1.2. Выбранный стек и почему
| Область | Выбор | Почему (для этого студента) | Отклонено |
|---|---|---|---|
| Язык/сборка | Kotlin 2.4, Gradle Kotlin DSL | Стандарт; K2-компилятор | Maven (плохо ложится на Kotlin-плагины) |
| Фреймворк | **Ktor** (Netty) | Явная композиция вместо магии: видно, что делает Spring | Spring (вынесен в модуль 14), Micronaut/Quarkus/http4k — лишние |
| DI | Ручной (constructor injection, composition root) | Студент видит, что такое «контекст» без контейнера | Koin — только упоминание в docs |
| БД-доступ | Raw JDBC → **Exposed (DSL, JDBC)** + HikariCP + **Flyway** | Лёгкий Kotlin-first; контраст с JPA | JPA/Hibernate (разбор в docs и модуле 14), jOOQ (упоминание), R2DBC (не нужен) |
| Redis | Lettuce (+ coroutines API) | Тот же клиент, что в Spring Data Redis | Jedis, Redisson |
| Kafka | `kafka-clients` + собственная тонкая обёртка | Видно, что скрывает `@KafkaListener` | spring-kafka (упоминание в модуле 14) |
| Сериализация | **kotlinx.serialization** (JSON) | Kotlin-native, без рефлексии | Jackson (сравнение в docs модуля 9; встретится в Spring) |
| Конкурентность | kotlinx.coroutines | Основа Kotlin-бэкенда | Reactor/RxJava |
| Тесты | JUnit 5 + kotlin-test + **MockK** + kotest-assertions-core + coroutines-test + `testApplication` + Testcontainers | Привычный JUnit, минимум нового | Kotest как раннер |
| Логи | kotlin-logging + Logback + logstash-logback-encoder (JSON для ELK) | Совпадает с его ELK-стеком | — |
| Метрики | Micrometer + Prometheus registry | Совпадает с Prometheus/Grafana | — |
| Auth | `ktor-server-auth-jwt`, токены Keycloak (в тестах — локальные RSA-ключи) | Знакомая модель Keycloak | — |

### 1.3. Что в Kotlin устроено иначе, чем в Java (обязательные темы)
null-safety и platform types; final по умолчанию и `open`; свойства вместо getter/setter; `data class` (≠ Java `record`), `value class`, `sealed`, `object`/`companion` вместо `static`; выражения (`if`/`when`/`try` возвращают значение); аргументы по умолчанию и именованные вместо перегрузок и builder'ов; extension-функции; лямбды и scope-функции; операторы и DSL; делегаты свойств; read-only vs mutable коллекции и `Sequence`; **нет checked exceptions**, `Result`/sealed-ошибки; `use` вместо try-with-resources; корутины и structured concurrency вместо потоков/`CompletableFuture`; interop-аннотации (`@Jvm*`, `@Throws`); `kotlin-spring`/`kotlin-jpa` (allopen/noarg).

### 1.4. Что сознательно не включено
Kotlin Multiplatform, Android/Compose, Kotlin/JS/Native/Wasm, Arrow (упоминание в `docs/99`), KSP и написание компиляторных плагинов, Kotlin scripting, reactive-стек Spring (WebFlux/Reactor — только упоминание `awaitSingle`), GraalVM native. Context parameters (стабильны с 2.4) и explicit backing fields — только в шпаргалках модулей 3 и 5, без упражнений.
**Добавлено сверх очевидного:** Java↔Kotlin interop и миграция (модули 2 и 14), kata по ревью кода (модуль 14), outbox + идемпотентность (модуль 13) — потому что именно это встречается в FinTech-проектах.

### 1.5. Таблица аналогий Spring → Kotlin/Ktor (общая; детали — в шпаргалках модулей 10, 11, 14)
| Spring | Ktor / Kotlin |
|---|---|
| `@SpringBootApplication` + autoconfig | `fun Application.module()` + явная установка плагинов |
| `@RestController` / `@GetMapping` | `routing { get("/x") { } }` |
| `@RequestBody` / `ResponseEntity` | `call.receive<T>()` / `call.respond(status, body)` |
| `@ControllerAdvice` | плагин `StatusPages` |
| `Filter` / `HandlerInterceptor` | плагины / `intercept` / `createApplicationPlugin` |
| `@Valid` + Bean Validation | плагин `RequestValidation` / явные валидаторы |
| `@Component` + `@Autowired` | ручной composition root, конструкторы |
| `@Configuration` / `@Bean` | функции-фабрики, `buildDependencies()` |
| `application.yml` + `@ConfigurationProperties` | `application.yaml` + типизированный `AppConfig` |
| `@Transactional` | `suspendTransaction { }` (Exposed) / свой `TxRunner` |
| Spring Data JPA | Exposed DSL, репозиторий вручную |
| `@Async` / `@Scheduled` / `CompletableFuture` | корутины, `launch`, `delay`-циклы |
| `@KafkaListener` | цикл `poll` в корутине (модуль 13) |
| `RedisTemplate` | Lettuce coroutines API |
| Actuator (`/health`, `/prometheus`) | свои роуты + Micrometer (модуль 11) |
| Spring Security + Keycloak adapter | `authenticate("jwt") { }` + JWKS |
| `MockMvc` / `@SpringBootTest` | `testApplication { }` |
| `@MockBean` | MockK / ручные fake'и |
| Jackson | kotlinx.serialization |
| Lombok / records | data class, value class, свойства |

---

## 2. Правила для агента

### 2.1. Общие
- Один модуль за раз; к следующему не переходить, пока не выполнен gate модуля (§7).
- Любой код, версию и API проверяй по документации, не по памяти.
- Не использовать: Lombok-подобные генераторы, Koin/Arrow/Kotest-runner, Spring вне `spring-bridge/`, `!!` и `GlobalScope` в эталонных решениях (кроме специально помеченных «плохих» примеров).
- Эталонные решения идиоматичны: `val` по умолчанию, выражения вместо операторов, отсутствие Java-измов.
- Каждый учебный файл ≤ ~150 строк, одна идея на файл.

### 2.2. Заглушки
- Заглушка **компилируется** и тела функций — `TODO("m03.2.1")` (выбрасывает `NotImplementedError`). Весь проект с заглушками обязан собираться: `./gradlew build -x test` зелёный.
- Сигнатуры, KDoc и `data class`-ы-контракты задаются сразу; студент заменяет только тела.
- Шапка каждого учебного файла (KDoc):

```kotlin
/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 3 · УПРАЖНЕНИЕ 3.3 — Money
 * ═══════════════════════════════════════════════════════════
 *  Тема:           data class, init-валидация, companion object
 *  Java-аналогия:  record + статические фабрики + equals/hashCode
 *  Тесты:          ./gradlew :core:test -Pmodule=03 --tests "ledger.m03.MoneyTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ        — что сделать, 1–3 предложения
 *  ТРЕБОВАНИЯ     — нумерованный список
 *  ПОДСКАЗКИ      — раскрываются по одной
 *  ЛОВУШКА        — вопрос про типичную ошибку
 * ═══════════════════════════════════════════════════════════
 */
```

### 2.3. Тесты
- Пакет `ledger.mNN`, класс `<Topic>Test`, аннотация `@Tag("mNN")`. Интеграционные дополнительно `@Tag("integration")`.
- Запуск модуля: `./gradlew test -Pmodule=NN` (в `build.gradle.kts` корня: если задано свойство `module`, JUnit Platform включает только тег `mNN`). Скрипт-обёртка `scripts/test-module.sh NN`.
- Имена тестов — предложения в backticks: `` `rejects transfer to the same account` ``. Сообщения assert'ов подсказывают, что не так.
- Тесты модуля N используют только API модулей ≤ N и **провайденные** файлы (§4.3).
- Без решений тесты падают по `NotImplementedError`, а не из-за ошибок компиляции.
- Асинхронные тесты — `runTest` (виртуальное время), без `Thread.sleep`.

### 2.4. Решения и проверка
- Решения лежат в `solutions/` с зеркальной структурой (`solutions/core/src/main/kotlin/...`). В основных папках решений быть не должно.
- `scripts/verify-solutions.sh`: копирует репозиторий во временную папку, накладывает `solutions/`, запускает `./gradlew test` (включая интеграционные). Должен быть полностью зелёным.
- `scripts/progress.sh`: печатает пройденные/всего тестов по модулям (разбор XML-отчётов JUnit).

### 2.5. Артефакты на каждый модуль
1. `docs/NN-<topic>.md` — шпаргалка ≤ 1.5 страницы: ключевые идеи, таблица «Java/Spring → Kotlin», 3–5 ловушек. Содержание перечислено в файле модуля.
2. `quizzes/NN-<topic>.md` — 6–8 вопросов «что выведет/почему не скомпилируется», ответы в `<details>`. Темы и примеры указаны в файле модуля.
3. Раздел в `PROGRESS.md` — чеклист упражнений.
4. «Веха» — что студент запускает руками.

---

## 3. План обучения

| № | Модуль | Ключевые темы | Часы | Веха |
|---|---|---|---|---|
| 00 | [Старт и инструменты](modules/00-setup.md) | Gradle KTS, JUnit/MockK, как компилируется Kotlin, первый interop | 2 | Проект собирается, первый Java↔Kotlin вызов |
| 01 | [Синтаксис и выражения](modules/01-syntax.md) | `val/var`, `when`, функции, строки, циклы | 3 | CLI считает и форматирует суммы |
| 02 | [Null-safety и Java interop](modules/02-null-safety.md) | `?`, `?:`, smart cast, platform types, `lazy` | 3 | Безопасная обёртка над Java-справочником |
| 03 | [Типы и модель домена](modules/03-types.md) | data/value/sealed/enum/object, наследование, делегирование | 5 | Доменная модель Money/Account/ошибки |
| 04 | [Коллекции и Sequence](modules/04-collections.md) | map/filter/fold/groupBy, ленивые последовательности | 5 | Выписка и отчёты по проводкам |
| 05 | [Функции, расширения, DSL](modules/05-functions.md) | лямбды, extensions, scope-функции, операторы, DSL, делегаты | 6 | `transferRequest { }`, `Money + Money` |
| 06 | [Ошибки и Result](modules/06-errors.md) | исключения, `Result`, sealed-исход, `use`, чистый движок переводов | 4 | CLI-сценарий переводов |
| 07 | [Корутины](modules/07-coroutines.md) | suspend, structured concurrency, отмена, retry | 7 | Параллельные вызовы с ретраями |
| 08 | [Flow, каналы, общее состояние](modules/08-flow-and-state.md) | Flow, Channel, Mutex, Shared/StateFlow | 5 | Конкурентные переводы без гонок |
| 09 | [Сериализация и конфигурация](modules/09-serialization-config.md) | kotlinx.serialization, DTO, типизированный конфиг | 4 | JSON-контракты и конфиг из env |
| 10 | [Голый HTTP → Ktor](modules/10-http-ktor.md) | свой роутер, Ktor routing, StatusPages, `testApplication` | 6 | REST API в памяти |
| 11 | [Ktor для продакшена](modules/11-ktor-production.md) | JWT/Keycloak, метрики, health, shutdown, логи | 7 | Защищённый наблюдаемый сервис |
| 12 | [База данных](modules/12-database.md) | JDBC, HikariCP, Flyway, Exposed, транзакции, N+1 | 8 | Сервис на Postgres |
| 13 | [Redis, идемпотентность, Kafka, outbox](modules/13-redis-kafka.md) | Lettuce, outbox, producer/consumer | 8 | E2E: переводы → outbox → Kafka → проекция |
| 14 | [Spring Boot + Kotlin, ревью, миграция](modules/14-spring-bridge.md) | перенос среза, kata ревью, Java→Kotlin | 6 | Тот же срез на Spring Boot 4 |
| | **Итого** | | **≈ 79** | |

### Дидактические принципы
1. Контраст с Java в каждой шпаргалке. 2. Ловушки — отдельные упражнения/квизы. 3. Чистый код до фреймворков (модули 1–9 без HTTP). 4. Эволюция одного кода: `Money.add` → оператор `+`; блокирующая логика → `suspend`; in-memory → Postgres; прямой вызов → outbox. 5. Каждая «магия» Spring сначала делается руками.

---

## 4. Проект LedgerKt

### 4.1. Описание
Бэкенд-сервис учётных записей и переводов (FinTech): счета, двойная запись проводок, переводы с идемпотентностью, события `TransferCompleted` через outbox в Kafka, проекция оборотов, JWT-авторизация, метрики и health-проверки.

### 4.2. Структура репозитория
```
ledger-kt/
├─ README.md  PROGRESS.md  plan.md(копия)  compose.yaml
├─ settings.gradle.kts  build.gradle.kts  gradle.properties  gradle/libs.versions.toml
├─ docs/  NN-*.md, 99-next-steps.md        quizzes/  NN-*.md
├─ scripts/  verify-solutions.sh  test-module.sh  progress.sh
├─ core/            # чистый Kotlin: домен, коллекции, корутины, сериализация (модули 0–9) + Java-legacy
│  └─ src/{main,test}/{kotlin,java}/ledger/...
├─ service/         # Ktor-сервис (модули 10–13)
├─ spring-bridge/   # Spring Boot 4 + Kotlin (модуль 14)
├─ migration-kata/  # mixed Java/Kotlin kata (модуль 14)
└─ solutions/{core,service,spring-bridge,migration-kata}/...
```
Основные пакеты: `ledger.basics`, `ledger.nullsafety`, `ledger.domain`, `ledger.collections`, `ledger.functional`, `ledger.errors`, `ledger.coroutines`, `ledger.state`, `ledger.serialization`, `ledger.config`, `ledger.http`, `ledger.persistence`, `ledger.messaging`.

### 4.3. Готовые части (пишет агент целиком; студент не трогает)
- Сборка: Gradle-файлы, wrapper, version catalog, `compose.yaml` (Postgres 16/17, Redis 7, Kafka в режиме KRaft).
- Data-only контракты модели: `LedgerEntry`, `Direction`, `Transfer`, `TransferStatus`, `TransferResult`, интерфейсы репозиториев и `EventPublisher`/`IdempotencyStore` (появляются в нужном модуле).
- Тестовые хелперы: `TestData` (билдеры счетов/проводок), `TestJwt` (RSA-ключи, подпись, JWKS), `PostgresFixture`/`RedisFixture`/`KafkaFixture` (синглтоны Testcontainers), `QueryCounter`.
- Java-legacy: `LegacyFeeCalculator.java`, `LegacyCustomerDirectory.java` (модули 0, 2).
- `logback.xml` (JSON), `Dockerfile`, `application.yaml`.

### 4.4. Модель данных
`Currency{USD,EUR,GBP,JPY}` · `Money(minor: Long, currency)` · `AccountId`, `TransferId`, `IdempotencyKey` (value class) · `Account(id, ownerName, status, balance, version)` · `AccountStatus{ACTIVE,FROZEN,CLOSED}` · `LedgerEntry(id, transferId, accountId, counterparty, direction, amount, createdAt)` · `Transfer(id, from, to, amount, status, idempotencyKey, createdAt)` · `sealed TransferError` · `sealed DomainEvent` (`TransferCompleted`, `TransferRejected`, `AccountFrozen`).

---

## 5. Спецификации модулей
Файлы `modules/00-setup.md` … `modules/14-spring-bridge.md` (ссылки в таблице §3). Структура каждого файла фиксирована: Цель → Предусловия → Шпаргалка → Упражнения (таблица: файл · что реализовать с сигнатурой · что проверяют тесты) → Рефакторинги → Квиз → Веха → Чеклист → Gate.

---

## 6. Итоговые документы
- **README.md:** цель, быстрый старт (`./gradlew build -x test`, `docker compose up -d`), как проходить модуль, как пользоваться подсказками/квизами, правило «решения — после попытки», таблица §3, FAQ (Docker, порты, Testcontainers на macOS/WSL).
- **PROGRESS.md:** чеклист по упражнениям, строка на файл.
- **docs/99-next-steps.md:** Arrow (Either/Raise), Kotest, ktlint/detekt, Spring + корутины (`awaitSingle`, `suspend` в MVC/WebFlux), Exposed R2DBC, виртуальные потоки vs корутины, KMP, что нового в Kotlin 2.5 (companion extensions — стабильны в 2.5.0-Beta), чтение исходников Ktor.
- **docs/00-java-to-kotlin-cheatsheet.md:** единая таблица синтаксиса Java → Kotlin (1 страница).

---

## 7. Definition of Done и порядок работы агента

### 7.1. Цикл на каждый модуль (строго по порядку)
1. Прочитай `modules/NN-*.md` целиком (другие модули не нужны, кроме перечисленных в «Предусловиях»).
2. Напиши **эталонное решение** в `solutions/` и **тесты** (`@Tag("mNN")`).
3. Прогони `./gradlew test -Pmodule=NN` на решениях → зелёный.
4. Сгенерируй **заглушки** в основных папках с шапками (§2.2), шпаргалку, квиз, раздел PROGRESS.
5. Убедись: `./gradlew build -x test` зелёный; `./gradlew test -Pmodule=NN` на заглушках падает только на `NotImplementedError`.
6. Прогони `scripts/verify-solutions.sh` для модулей 0..NN (не только NN) — регрессия не допускается.
7. Только после этого переходи к следующему модулю.

### 7.2. Критерии готовности
- [ ] Структура §4.2 создана, `./gradlew build -x test` зелёный из чистого клона.
- [ ] Для каждого модуля 00–14: заглушки с шапками, тесты, `docs/NN`, `quizzes/NN`, раздел PROGRESS, эталон в `solutions/`.
- [ ] `scripts/verify-solutions.sh` полностью зелёный (включая интеграционные тесты в Docker).
- [ ] Все темы из §1.3 встречаются хотя бы в одном упражнении (таблица покрытия в `docs/COVERAGE.md`: тема → упражнение).
- [ ] Версии в `libs.versions.toml` сверены с актуальными; отклонения записаны ниже.
- [ ] README, PROGRESS, `docs/99-next-steps.md`, cheatsheet написаны.

### 7.3. Отклонения (заполняет агент по ходу)
_Пока пусто._
