# Квиз 08 — Flow, каналы и общее состояние

## 1. Чем `Flow` отличается от `SharedFlow`?
<details><summary>Ответ</summary>`Flow` холодный: код выполняется заново на каждый `collect`. `SharedFlow` горячий: подписчики получают общий поток событий после подписки; без replay старые события не доставляются.</details>

## 2. Что произойдёт с событием, опубликованным в `MutableSharedFlow(replay = 0)` без подписчиков?
<details><summary>Ответ</summary>Оно будет потеряно. `emit` не зависает; `tryEmit` может вернуть `true`. Для гарантированной доставки нужен буфер/replay/Channel/брокер.</details>

## 3. Чем `StateFlow` отличается от `SharedFlow`?
<details><summary>Ответ</summary>У `StateFlow` всегда есть текущее значение, он конфлятит и emits только при изменении значения (distinctUntilChanged). `SharedFlow` — поток событий, может не иметь значения и не конфлятит по умолчанию.</details>

## 4. Почему `_state.value = _state.value + x` небезопасно?
<details><summary>Ответ</summary>Это чтение-изменение-запись: два конкурентных обновления могут прочитать одно и то же значение и потерять одно изменение. Нужен `update { }`, который атомарно применяет функцию.</details>

## 5. Что делает `Channel(capacity = 0)`?
<details><summary>Ответ</summary>Создаёт rendezvous-канал: `send` приостанавливается, пока receiver не заберёт элемент. Без активного consumer'а отправитель будет ждать бесконечно.</details>

## 6. Чем `Mutex` отличается от `synchronized`?
<details><summary>Ответ</summary>`Mutex` — suspend-блокировка: ожидание не блокирует поток, а приостанавливает корутину. `Mutex` не реентерабелен, `synchronized` в Java — реентерабелен. `synchronized` нельзя использовать в suspend-функции без блокировки потока.</details>

## 7. Зачем `ConcurrentHashMap.computeIfAbsent` для per-key блокировок?
<details><summary>Ответ</summary>Он атомарно создаёт ровно один `Mutex` на ключ даже при конкурентных вызовах. `getOrPut` на обычной `Map` не атомарен и может создать два разных мьютекса.</details>

## 8. Что выведет?
```kotlin
runTest {
    val flow = flow { println("start"); emit(1) }
    flow.collect { println("first $it") }
    flow.collect { println("second $it") }
}
```
<details><summary>Ответ</summary>`start`, `first 1`, `start`, `second 1`. Холодный Flow выполняет upstream заново на каждый collect.</details>
