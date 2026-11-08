package ledger.state

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ledger.domain.DomainEvent

class TransferEventBus(
    replay: Int = 0,
    extraBufferCapacity: Int = 64,
) {
    private val mutableEvents = MutableSharedFlow<DomainEvent>(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity,
    )

    val events: SharedFlow<DomainEvent> = mutableEvents.asSharedFlow()

    suspend fun publish(event: DomainEvent) {
        mutableEvents.emit(event)
    }

    fun tryPublish(event: DomainEvent): Boolean = mutableEvents.tryEmit(event)
}
