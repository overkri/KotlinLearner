package ledger.m13

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import ledger.messaging.EventPublisher
import ledger.messaging.InMemoryOutboxStore
import ledger.messaging.OutboxPublisher
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m13")
class OutboxPublisherTest {

    private val fixed = Instant.parse("2026-05-01T10:00:00Z")

    private class RecordingPublisher : EventPublisher {
        val payloads = mutableListOf<String>()

        override suspend fun publish(payload: String) {
            payloads += payload
        }
    }

    @Test
    fun `publishes and marks pending messages`() = runTest {
        val store = InMemoryOutboxStore()
        repeat(3) { index ->
            store.append("event-$index", "transfer_completed", """{"n":$index}""", fixed)
        }
        val publisher = RecordingPublisher()
        val outbox = OutboxPublisher(store, publisher) { fixed }

        val published = outbox.publishPending(limit = 2)

        published shouldBe 2
        publisher.payloads shouldBe listOf("""{"n":0}""", """{"n":1}""")
        store.findUnpublished(10).map { it.eventId } shouldBe listOf("event-2")
        store.all().take(2).all { it.publishedAt == fixed } shouldBe true
    }

    @Test
    fun `does not mark a message when publishing fails`() = runTest {
        val store = InMemoryOutboxStore()
        store.append("event-1", "transfer_completed", "one", fixed)
        store.append("event-2", "transfer_completed", "two", fixed)
        val calls = mutableListOf<String>()
        val failing = EventPublisher { payload ->
            calls += payload
            if (payload == "two") error("kafka down")
        }
        val outbox = OutboxPublisher(store, failing) { fixed }

        val error = shouldThrow<IllegalStateException> { outbox.publishPending() }

        error shouldHaveMessage "kafka down"
        calls shouldBe listOf("one", "two")
        store.findUnpublished(10).map { it.eventId } shouldBe listOf("event-2")
    }

    @Test
    fun `rejects a non-positive limit`() = runTest {
        val outbox = OutboxPublisher(InMemoryOutboxStore(), RecordingPublisher())

        val error = shouldThrow<IllegalArgumentException> { outbox.publishPending(0) }
        error shouldHaveMessage "limit must be positive"
    }

    @Test
    fun `returns zero when there is nothing to publish`() = runTest {
        OutboxPublisher(InMemoryOutboxStore(), RecordingPublisher()).publishPending() shouldBe 0
    }
}
