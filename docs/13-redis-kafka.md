# 13 — Redis, идемпотентность, Kafka, outbox

## Цель
Закрыть распределённую часть LedgerKt: идемпотентность переводов через Redis, транзакционный outbox, Kafka producer/consumer и проекция событий.

## Предусловия
Модули 10–12. Docker запущен. `IdempotencyStore`, `CachedTransferDto`, `OutboxStore`, `InMemoryOutboxStore`, `EventPublisher`, `RedisFixture`, `KafkaFixture` предоставлены.

## Шпаргалка
- **Идемпотентность**: клиент присылает `Idempotency-Key`; повтор возвращает тот же результат, delegate не вызывается.
- **Redis** — кэш с TTL, не источник истины. Ключ с namespace (`idem:`), сериализованный снимок результата.
- **Outbox**: событие пишется в ту же БД-транзакцию, что и бизнес-изменение; отдельный publisher отправляет и помечает `published_at`.
- **At-least-once**: publisher может отправить событие повторно; consumer должен быть идемпотентным.
- **Kafka producer**: `send(ProducerRecord).get()` ждёт ack; `acks=all` + идемпотентный producer.
- **Kafka consumer**: `poll` в корутине, `commitSync` после успешного handler; `wakeup()` для остановки.
- **Dual-write problem**: нельзя атомарно записать в БД и Kafka; outbox решает это ценой задержки и повторной доставки.
- **Lettuce coroutines** требует `kotlinx-coroutines-reactive` в runtime.

## Java → Kotlin / Spring
| Java/Spring | Kotlin |
|---|---|
| `RedisTemplate` | Lettuce `RedisCoroutinesCommands` |
| `@Cacheable` / Redis cache | `IdempotencyStore` + TTL |
| `@KafkaListener` | `poll`-цикл в корутине |
| `KafkaTemplate.send` | `KafkaProducer.send(...).get()` в `Dispatchers.IO` |
| `@Transactional` + event publish | outbox в той же транзакции |
| Debezium outbox | свой `OutboxPublisher` |
| `AckMode.MANUAL` | `commitSync()` после handler |
| `@RetryableTopic` | идемпотентный consumer + retry |

## Идемпотентность: что хранить
1. `get(key)` -> если есть готовый результат, вернуть его.
2. Иначе выполнить операцию.
3. При успехе сохранить снимок (`CachedTransferDto`) с TTL.
4. Failure не кэшировать: клиент может исправить запрос/повторить.

В реальном Stripe-подобном API есть состояние «в процессе», чтобы параллельные дубли не выполнили операцию дважды. В курсе показан базовый вариант; гонка отмечена в «Ловушке».

## Outbox-пайплайн
```
TransferEngine + БД-транзакция
        └─ outbox(event)  [та же транзакция]
OutboxPublisher (poll)
        └─ KafkaEventPublisher -> Kafka topic
KafkaEventConsumer (poll + commit)
        └─ проекция/побочные эффекты
```
- `markPublished` только после успешной отправки.
- При падении между send и mark будет повторная доставка — это нормально для at-least-once.
- Consumer обязан быть идемпотентным (dedup по `eventId`).

## Пять ловушек
1. **Dual write**. `save DB` + `send Kafka` без outbox: при падении между ними событие теряется или отправляется без изменения. Outbox — обязательный паттерн.
2. **Auto-commit в Kafka**. Offset коммитится до обработки; падение handler теряет сообщение. Нужен manual commit после успеха.
3. **`Thread.sleep`/blocking в poll-цикле**. Блокирует поток; poll и handler должны быть в IO-диспетчере, а тяжёлую работу — вне poll.
4. **TTL меньше времени ретраев**. Ключ истечёт, повтор создаст дубль. TTL должен покрывать окно повторных попыток клиента.
5. **Высокая кардинальность метрик/ключей**. `idem:<uuid>` — нормально; `idem:<accountId>:<timestamp>` без TTL — мусор в Redis. Следите за памятью и eviction policy.
