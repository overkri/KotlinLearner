package ledger.m13

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ledger.messaging.KafkaEventConsumer
import ledger.testing.KafkaFixture
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.UUID

@Tag("m13")
@Tag("integration")
class KafkaEventConsumerTest {

    @Test
    fun `consumes messages in order`() {
        runBlocking {
            val topic = "ledger-${UUID.randomUUID()}"
            KafkaFixture.createTopic(topic)

            KafkaFixture.producer().use { producer ->
                producer.send(ProducerRecord(topic, "one")).get()
                producer.send(ProducerRecord(topic, "two")).get()
            }

            val consumer = KafkaFixture.consumer("consumer-${UUID.randomUUID()}")
            val eventConsumer = KafkaEventConsumer(consumer, topic)
            val received = Channel<String>(Channel.UNLIMITED)
            val job = eventConsumer.start(this) { received.send(it) }

            try {
                withTimeout(60_000) {
                    val values = listOf(received.receive(), received.receive())
                    values shouldBe listOf("one", "two")
                }
            } finally {
                eventConsumer.close()
                job.cancel()
            }
        }
    }
}
