# Что дальше после курса

Курс заканчивается на рабочем срезе LedgerKt, но Kotlin-бэкенд этим не ограничивается. Ниже — направления, которые стоит освоить следующими.

## 1. Arrow / функциональные ошибки
- `Either<L, R>` и `Raise` для ошибок без исключений и без `Result`.
- `NonEmptyList`, `Validated` (аккумуляция ошибок валидации).
- Когда Arrow оправдан, а когда sealed-типы проще.

## 2. Kotest как раннер
- Property-based testing (`checkAll`, `forAll`).
- `StringSpec`/`BehaviorSpec` и мягкие assertions.
- В курсе Kotest используется только для assertions, чтобы не менять привычный JUnit.

## 3. Качество кода
- `ktlint`/`detekt` в CI, baseline и правила для команды.
- `explicitApi()` для библиотек.
- Konsist — архитектурные тесты (запрет зависимостей между слоями).

## 4. Spring + корутины
- `suspend`-контроллеры в Spring MVC (поддержка с 6.0) и WebFlux.
- `awaitSingle`, `awaitBody`, `awaitAll` для Reactor/WebClient.
- `@Transactional` и корутины: transaction context propagation, `TransactionalOperator`.
- Виртуальные потоки (JDK 21+) vs корутины: когда что выбирать.

## 5. Базы данных
- Exposed R2DBC / Spring Data R2DBC.
- jOOQ + Kotlin DSL.
- Пул соединений на pod: PgBouncer, лимиты, таймауты.
- Outbox + Debezium CDC вместо polling publisher.
- Тестирование миграций: testcontainers + Flyway, parallel test schemas.

## 6. Kafka в проде
- Идемпотентный producer, транзакции Kafka (`read_committed`), exactly-once в рамках Kafka.
- Consumer groups, rebalance listeners, cooperative sticky assignor.
- Retry topics / dead letter queue, poison messages.
- Schema Registry + Avro/Protobuf.

## 7. Kotlin Multiplatform и другие платформы
- KMP для shared-модулей (валидация, DTO).
- Kotlin/JS и Compose Multiplatform — если появится фронтенд.
- Kotlin/Native и Wasm — обзорно.

## 8. Kotlin 2.5 и новые возможности
- Companion extensions (стабильны в 2.5.0-Beta): расширения для companion object.
- Context parameters: уже стабильны с 2.4; осторожно с API-дизайном.
- Новые возможности K2: быстрее, строже, лучше диагностика.

## 9. Чтение исходников
- Ktor: `Application`, `Pipeline`, `routing`, `StatusPages` — как устроены плагины.
- Exposed: `Query`, `Transaction`, `Statement` — какой SQL генерируется.
- kotlinx.coroutines: `Job`, `CoroutineScope`, `Mutex`, `StateFlow`.
- Spring Kotlin extensions: `kotlin-spring`, `kotlin-jpa`, `kotlin-reflect`.

## 10. Проектные идеи
1. **LedgerKt v2**: добавить реальную Keycloak-аутентификацию, Postgres-репозитории в HTTP, outbox в той же транзакции.
2. **Микросервисный срез**: transfer-service + notification-consumer через Kafka.
3. **Нагрузочный тест**: Gatling/k6, метрики p99, пул соединений, backpressure.
4. **CI/CD**: GitHub Actions, Testcontainers, публикация Docker-образа, миграции на старте.
5. **Review bot**: detekt + ktlint + архитектурные правила в PR.

Главное — не останавливаться на «синтаксис выучен»: Kotlin-бэкенд раскрывается в дизайне API, конкурентности и эксплуатации.
