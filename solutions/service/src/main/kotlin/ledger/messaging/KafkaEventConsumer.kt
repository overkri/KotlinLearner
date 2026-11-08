package ledger.messaging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration

class KafkaEventConsumer(
    private val consumer: KafkaConsumer<String, String>,
    private val topic: String,
) : AutoCloseable {

    fun start(scope: CoroutineScope, handler: suspend (String) -> Unit): Job =
        scope.launch(Dispatchers.IO) {
            consumer.subscribe(listOf(topic))
            try {
                while (isActive) {
                    val records = consumer.poll(Duration.ofMillis(100))
                    for (record in records) {
                        handler(record.value())
                        consumer.commitSync()
                    }
                }
            } catch (_: WakeupException) {
                // close() was called; leave the loop cleanly
            } finally {
                runCatching { consumer.close() }
            }
        }

    override fun close() {
        consumer.wakeup()
    }
}
