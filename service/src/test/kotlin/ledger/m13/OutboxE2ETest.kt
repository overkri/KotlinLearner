package ledger.m13

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ledger.messaging.InMemoryOutboxStore
import ledger.messaging.KafkaEventPublisher
import ledger.messaging.OutboxPublisher
import ledger.testing.KafkaFixture
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.UUID

@Tag("m13")
@Tag("integration")
class OutboxE2ETest {

    @Test
    fun `outbox message reaches Kafka and is marked published`() {
        runBlocking {
            withTimeout(60_000) {
                val topic = "ledger-${UUID.randomUUID()}"
                KafkaFixture.createTopic(topic)
                val store = InMemoryOutboxStore()
                store.append(
                    eventId = "event-1",
                    eventType = "transfer_completed",
                    payload = """{"event":"transfer_completed","id":"tr-1"}""",
                    createdAt = Instant.parse("2026-05-01T10:00:00Z"),
                )

                KafkaFixture.producer().use { producer ->
                    val publisher = KafkaEventPublisher(producer, topic)
                    val outbox = OutboxPublisher(store, publisher)
                    outbox.publishPending() shouldBe 1
                }

                store.findUnpublished(10) shouldBe emptyList()

                KafkaFixture.consumer("e2e-${UUID.randomUUID()}").use { consumer ->
                    consumer.subscribe(listOf(topic))
                    val values = mutableListOf<String>()
                    val deadline = System.currentTimeMillis() + 30_000
                    while (values.isEmpty() && System.currentTimeMillis() < deadline) {
                        consumer.poll(Duration.ofMillis(200))
                            .forEach { values += it.value() }
                    }
                    values shouldContain """{"event":"transfer_completed","id":"tr-1"}"""
                }
            }
        }
    }
}
