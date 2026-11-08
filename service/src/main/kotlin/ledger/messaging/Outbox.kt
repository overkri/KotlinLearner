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

/**
 * In-memory outbox для юнит-тестов. Предоставляется готовым.
 */
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

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 13 · УПРАЖНЕНИЕ 13.3 — Outbox publisher
 * ═══════════════════════════════════════════════════════════
 *  Тема:           transactional outbox, at-least-once, mark published
 *  Java-аналогия:  Debezium/outbox pattern
 *  Тесты:          ./gradlew :service:test -Pmodule=13 --tests "ledger.m13.OutboxPublisherTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Публикуйте неопубликованные outbox-сообщения и отмечайте успешные.
 *
 *  ТРЕБОВАНИЯ
 *  1. publishPending(limit): взять не более limit неопубликованных.
 *  2. Для каждого: publisher.publish(payload); затем markPublished(id, now()).
 *  3. Если publish бросил исключение — не отмечать сообщение и пробросить
 *     исключение (at-least-once: лучше повтор, чем потеря).
 *  4. Вернуть количество успешно опубликованных.
 *  5. limit <= 0 -> IllegalArgumentException "limit must be positive".
 *
 *  ПОДСКАЗКИ
 *  - `store.findUnpublished(limit)`.
 *  - `clock()` для publishedAt; инъекция делает тест детерминированным.
 *  - Порядок сообщений — по id (контракт store).
 *
 *  ЛОВУШКА
 *  Почему публикация и запись в БД не могут быть одной атомарной операцией
 *  без outbox/2PC? Что такое dual-write problem?
 * ═══════════════════════════════════════════════════════════
 */
class OutboxPublisher(
    private val store: OutboxStore,
    private val publisher: EventPublisher,
    private val clock: () -> Instant = { Instant.now() },
) {
    suspend fun publishPending(limit: Int = 100): Int = TODO("m13.3.1")
}
