package ledger.messaging

import org.apache.kafka.clients.producer.KafkaProducer

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 13 · УПРАЖНЕНИЕ 13.4 — Kafka producer
 * ═══════════════════════════════════════════════════════════
 *  Тема:           KafkaProducer, send/get, IO dispatcher, close
 *  Java-аналог:    KafkaTemplate.send
 *  Тесты:          ./gradlew :service:test -Pmodule=13 --tests "ledger.m13.KafkaEventPublisherTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте EventPublisher поверх KafkaProducer.
 *
 *  ТРЕБОВАНИЯ
 *  1. publish(payload): отправить в topic, дождаться подтверждения (.get()),
 *     не блокируя корутинный поток — withContext(Dispatchers.IO).
 *  2. close(): закрыть producer.
 *  3. Ошибку отправки пробросить наружу (at-least-once).
 *  4. Не создавать producer внутри publish.
 *
 *  ПОДСКАЗКИ
 *  - `ProducerRecord(topic, payload)`.
 *  - `.get()` у Future бросает ExecutionException; дайте ей всплыть.
 *  - `acks=all` настраивается в producer properties, не в этом классе.
 *
 *  ЛОВУШКА
 *  Почему `.get()` на каждый send снижает throughput? Когда нужен батчинг
 *  и какая цена у «подтверждения каждого сообщения»?
 * ═══════════════════════════════════════════════════════════
 */
class KafkaEventPublisher(
    private val producer: KafkaProducer<String, String>,
    private val topic: String,
) : EventPublisher, AutoCloseable {

    override suspend fun publish(payload: String): Unit = TODO("m13.4.1")

    override fun close(): Unit = TODO("m13.4.2")
}
