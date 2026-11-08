package ledger.messaging

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class KafkaEventPublisher(
    private val producer: KafkaProducer<String, String>,
    private val topic: String,
) : EventPublisher, AutoCloseable {

    override suspend fun publish(payload: String) {
        withContext(Dispatchers.IO) {
            producer.send(ProducerRecord(topic, payload)).get()
        }
    }

    override fun close() {
        producer.close()
    }
}
