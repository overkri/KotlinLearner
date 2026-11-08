package ledger.messaging

import java.time.Instant

data class OutboxMessage(
    val id: Long,
    val eventId: String,
    val eventType: String,
    val payload: String,
    val createdAt: Instant,
    val publishedAt: Instant? = null,
)

interface OutboxStore {
    fun append(eventId: String, eventType: String, payload: String, createdAt: Instant): OutboxMessage
    fun findUnpublished(limit: Int): List<OutboxMessage>
    fun markPublished(id: Long, publishedAt: Instant)
}

class InMemoryOutboxStore : OutboxStore {

    private val messages = mutableListOf<OutboxMessage>()
    private var nextId = 1L

    override fun append(
        eventId: String,
        eventType: String,
        payload: String,
        createdAt: Instant,
    ): OutboxMessage {
        val message = OutboxMessage(
            id = nextId++,
            eventId = eventId,
            eventType = eventType,
            payload = payload,
            createdAt = createdAt,
        )
        messages += message
        return message
    }

    override fun findUnpublished(limit: Int): List<OutboxMessage> =
        messages.asSequence()
            .filter { it.publishedAt == null }
            .take(limit)
            .toList()

    override fun markPublished(id: Long, publishedAt: Instant) {
        val index = messages.indexOfFirst { it.id == id }
        require(index >= 0) { "outbox message not found: $id" }
        messages[index] = messages[index].copy(publishedAt = publishedAt)
    }

    fun all(): List<OutboxMessage> = messages.toList()
}

fun interface EventPublisher {
    suspend fun publish(payload: String)
}

class OutboxPublisher(
    private val store: OutboxStore,
    private val publisher: EventPublisher,
    private val clock: () -> Instant = { Instant.now() },
) {
    suspend fun publishPending(limit: Int = 100): Int {
        require(limit > 0) { "limit must be positive" }

        var published = 0
        for (message in store.findUnpublished(limit)) {
            publisher.publish(message.payload)
            store.markPublished(message.id, clock())
            published += 1
        }
        return published
    }
}
