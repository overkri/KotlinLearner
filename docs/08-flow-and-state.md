# 08 — Flow, каналы и общее состояние

## Цель
Научиться работать с горячими и холодными потоками: `SharedFlow` для событий, `StateFlow` для состояния, `Channel` для очередей с backpressure, `Mutex` для конкурентного доступа.

## Предусловия
Модуль 07. `DomainEvent`, `TransferRequest`, `LedgerEntry` готовы.

## Шпаргалка
- `Flow` — **холодный** поток: код выполняется заново на каждый `collect`.
- `SharedFlow` — **горячий** поток: подписчики получают только события после подписки; `replay` хранит последние N.
- `StateFlow` — горячее **состояние**: всегда есть текущее значение, конфляция, `distinctUntilChanged`.
- `MutableStateFlow.update { }` — атомарное обновление; `value = value + ...` — чтение-изменение-запись с гонкой.
- `Channel` — очередь с backpressure; `send` приостанавливается при заполнении, `receive`/`for` забирает элементы.
- `Mutex` — suspend-блокировка; **не реентерабельна**, в отличие от `ReentrantLock`.
- `ConcurrentHashMap.computeIfAbsent` — атомарное создание per-key lock.
- `runTest` + `testScheduler.currentTime` — виртуальное время.

## Hot vs cold
| | `Flow` | `SharedFlow` | `StateFlow` |
|---|---|---|---|
| Холодный | да | нет | нет |
| Начальное значение | нет | нет (есть replay) | всегда есть |
| Несколько подписчиков | каждый свой проход | общий поток | общее состояние |
| Конфляция | нет | настраивается | всегда |
| Типичный use case | запрос/отчёт | события | UI/кэш/статус |

`SharedFlow(replay = 0, extraBufferCapacity = 0)` без подписчиков просто теряет событие; `emit` при этом не зависает. `tryEmit` может вернуть `true`, даже если событие никто не получил.

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `Flow.Publisher` / Reactor `Flux` | `Flow` |
| `Sinks.Many` | `MutableSharedFlow` |
| `BehaviorSubject` | `MutableStateFlow` |
| `BlockingQueue` | `Channel` |
| `ReentrantLock` | `Mutex` |
| `ConcurrentHashMap.computeIfAbsent` | то же + `Mutex` |
| `AtomicReference.updateAndGet` | `MutableStateFlow.update` |
| `@EventListener` | `SharedFlow.collect` |

## Пять ловушек
1. **`SharedFlow` не гарантирует доставку**. При `replay=0` событие, опубликованное без подписчиков, исчезает. Для гарантий нужен `Channel`/Kafka/outbox, а не SharedFlow.
2. **`StateFlow` конфлятит**. Быстрые обновления могут «схлопнуться»; подписчик увидит только последнее значение. Для каждого события нужен `SharedFlow`/`Channel`.
3. **`Mutex` не реентерабелен**. Вложенный `withLock` на тот же ключ приведёт к deadlock. Для рекурсивных структур нужен другой дизайн.
4. **`Channel(capacity = 0)` — rendezvous**. `send` ждёт, пока кто-то не заберёт элемент; без consumer'а будет вечный suspend.
5. **`collect` в `launch` без scope**. Если запустить в `GlobalScope`, коллектор переживёт приложение и будет держать ресурсы. Используйте scope приложения/теста.
