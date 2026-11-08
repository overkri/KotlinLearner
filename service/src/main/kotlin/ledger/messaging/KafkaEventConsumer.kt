package ledger.messaging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.apache.kafka.clients.consumer.KafkaConsumer

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 13 · УПРАЖНЕНИЕ 13.5 — Kafka consumer
 * ═══════════════════════════════════════════════════════════
 *  Тема:           poll loop в корутине, manual commit, wakeup/close
 *  Java-аналогия:  @KafkaListener + ack mode MANUAL
 *  Тесты:          ./gradlew :service:test -Pmodule=13 --tests "ledger.m13.KafkaEventConsumerTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте consumer-loop, который обрабатывает сообщения и коммитит offset
 *  только после успешного handler.
 *
 *  ТРЕБОВАНИЯ
 *  1. start(scope, handler): подписаться на topic, запустить цикл в
 *     scope.launch(Dispatchers.IO), вернуть Job.
 *  2. Каждый poll -> forEach record -> handler(record.value()) -> commitSync().
 *  3. При отмене корутины цикл завершается и consumer закрывается.
 *  4. close(): разбудить consumer (wakeup), чтобы poll не висел.
 *  5. Не коммитить offset до успешной обработки.
 *
 *  ПОДСКАЗКИ
 *  - `consumer.subscribe(listOf(topic))`.
 *  - `consumer.poll(Duration.ofMillis(100))`.
 *  - `consumer.commitSync()`.
 *  - `consumer.wakeup()` в close; в цикле обработайте WakeupException.
 *
 *  ЛОВУШКА
 *  Почему auto-commit может потерять сообщение при падении handler? Как
 *  связаны at-least-once и идемпотентность consumer'а?
 * ═══════════════════════════════════════════════════════════
 */
class KafkaEventConsumer(
    private val consumer: KafkaConsumer<String, String>,
    private val topic: String,
) : AutoCloseable {

    fun start(scope: CoroutineScope, handler: suspend (String) -> Unit): Job = TODO("m13.5.1")

    override fun close(): Unit = TODO("m13.5.2")
}
