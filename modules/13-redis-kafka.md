# Модуль 13 — Redis, идемпотентность, Kafka, outbox

## Цель
Реализовать распределённые гарантии: идемпотентные переводы через Redis, транзакционный outbox, Kafka producer/consumer и E2E-пайплайн.

## Предусловия
Модули 10–12. Docker запущен. `IdempotencyStore`, `CachedTransferDto`, `OutboxStore`, `EventPublisher`, `RedisFixture`, `KafkaFixture` предоставлены.

## Шпаргалка
Redis TTL + namespace; idempotency key; outbox; at-least-once; `KafkaProducer.send().get()`; `poll` + `commitSync`; `wakeup`; `Dispatchers.IO`; дедупликация consumer'а.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `service/src/main/kotlin/ledger/messaging/Idempotency.kt` | `IdempotentTransferService.transfer` | Кэш success, отсутствие повтора delegate, failure не кэшируется, битый кэш, разные ключи |
| `service/src/main/kotlin/ledger/messaging/RedisIdempotencyStore.kt` | `get`, `put` через Lettuce coroutines | Get/put, namespace, overwrite, TTL > 0 |
| `service/src/main/kotlin/ledger/messaging/Outbox.kt` | `OutboxPublisher.publishPending` | Публикация, markPublished, остановка при ошибке, limit |
| `service/src/main/kotlin/ledger/messaging/KafkaEventPublisher.kt` | `publish`, `close` | Отправка в Kafka, ack |
| `service/src/main/kotlin/ledger/messaging/KafkaEventConsumer.kt` | `start`, `close` | Poll, порядок, commit, остановка |

## Рефакторинги
1. Добавьте состояние «in progress» в idempotency store и проверьте параллельные дубли.
2. Замените `commitSync` на батчевый `commitAsync` — какие гарантии теряются?
3. Добавьте dedup consumer'а по `eventId` в Redis и сделайте обработку exactly-once-подобной.

## Квиз
`quizzes/13-redis-kafka.md` — 8 вопросов про dual-write, outbox, commit, wakeup и TTL.

## Веха
E2E: перевод -> outbox -> Kafka -> consumer -> проекция; повторный запрос с тем же ключом не создаёт второй перевод.

## Чеклист
- [ ] 13.1 `IdempotentTransferService` решён.
- [ ] 13.2 `RedisIdempotencyStore` решён.
- [ ] 13.3 `OutboxPublisher` решён.
- [ ] 13.4 `KafkaEventPublisher` решён.
- [ ] 13.5 `KafkaEventConsumer` решён.
- [ ] `./gradlew test -Pmodule=13` зелёный (Docker).
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 13` — зелёный (Docker).
