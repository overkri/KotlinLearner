package ledger.state

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ledger.domain.DomainEvent

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 8 · УПРАЖНЕНИЕ 8.1 — Шина доменных событий
 * ═══════════════════════════════════════════════════════════
 *  Тема:           SharedFlow, replay, extraBufferCapacity, tryEmit
 *  Java-аналогия:  ApplicationEventPublisher / Kafka topic в памяти
 *  Тесты:          ./gradlew :core:test -Pmodule=08 --tests "ledger.m08.TransferEventBusTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте горячую шину событий: подписчики получают события, опубликованные
 *  после подписки; replay позволяет «догнать» последние N событий.
 *
 *  ТРЕБОВАНИЯ
 *  1. publish(event) — suspend-функция, использует emit.
 *     При отсутствии подписчиков событие теряется (SharedFlow hot),
 *     при медленных подписчиках emit приостанавливается.
 *  2. tryPublish(event) — не блокирует, возвращает результат tryEmit.
 *  3. events — read-only SharedFlow; снаружи нельзя emit.
 *  4. replay и extraBufferCapacity передаются в MutableSharedFlow.
 *
 *  ПОДСКАЗКИ
 *  - `MutableSharedFlow<T>(replay = replay, extraBufferCapacity = extraBufferCapacity)`.
 *  - `asSharedFlow()` отдаёт read-only view.
 *  - tryEmit вернёт false, если буфер заполнен, а подписчик не успевает.
 *
 *  ЛОВУШКА
 *  Чем SharedFlow отличается от StateFlow? Почему событие, опубликованное
 *  без подписчиков, исчезает, если replay = 0?
 * ═══════════════════════════════════════════════════════════
 */
class TransferEventBus(
    replay: Int = 0,
    extraBufferCapacity: Int = 64,
) {
    private val mutableEvents = MutableSharedFlow<DomainEvent>(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity,
    )

    val events: SharedFlow<DomainEvent> = mutableEvents.asSharedFlow()

    suspend fun publish(event: DomainEvent): Unit = TODO("m08.1.1")

    fun tryPublish(event: DomainEvent): Boolean = TODO("m08.1.2")
}
