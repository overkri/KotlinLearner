package ledger.m13

import io.kotest.matchers.collections.shouldContain
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ledger.messaging.KafkaEventPublisher
import ledger.testing.KafkaFixture
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.UUID

@Tag("m13")
@Tag("integration")
class KafkaEventPublisherTest {

    @Test
    fun `publishes a message to Kafka`() {
        runBlocking {
            withTimeout(60_000) {
                val topic = "ledger-${UUID.randomUUID()}"
                KafkaFixture.createTopic(topic)
                val payload = """{"event":"transfer_completed","id":"tr-1"}"""

                KafkaFixture.producer().use { producer ->
                    val publisher = KafkaEventPublisher(producer, topic)
                    publisher.publish(payload)
                }

                KafkaFixture.consumer("test-${UUID.randomUUID()}").use { consumer ->
                    consumer.subscribe(listOf(topic))
                    val values = mutableListOf<String>()
                    val deadline = System.currentTimeMillis() + 30_000
                    while (values.isEmpty() && System.currentTimeMillis() < deadline) {
                        consumer.poll(Duration.ofMillis(200))
                            .forEach { values += it.value() }
                    }
                    values shouldContain payload
                }
            }
        }
    }
}
