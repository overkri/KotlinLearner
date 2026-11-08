package ledger.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import ledger.domain.TransferRequest

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 8 · УПРАЖНЕНИЕ 8.4 — Очередь переводов
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Channel, backpressure, consumer loop, закрытие
 *  Java-аналогия:  BlockingQueue + ExecutorService
 *  Тесты:          ./gradlew :core:test -Pmodule=08 --tests "ledger.m08.TransferQueueTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте очередь заявок с одним consumer'ом.
 *
 *  ТРЕБОВАНИЯ
 *  1. submit(request) отправляет заявку в Channel и приостанавливается при
 *     заполненном буфере (backpressure).
 *  2. start(scope, handler) запускает consumer в переданном scope и возвращает Job.
 *  3. Consumer обрабатывает элементы по одному, пока канал не закрыт.
 *  4. close() закрывает канал; после дренажа consumer завершается.
 *  5. Использовать `for (request in channel)` или `consumeEach`.
 *
 *  ПОДСКАЗКИ
 *  - `Channel<TransferRequest>(capacity)`.
 *  - `scope.launch { for (request in channel) handler(request) }`.
 *  - `channel.send(request)` и `channel.close()`.
 *
 *  ЛОВУШКА
 *  Что произойдёт, если capacity = 0 (RENDEZVOUS) и consumer не запущен?
 *  Как это связано с backpressure и deadlock?
 * ═══════════════════════════════════════════════════════════
 */
class TransferQueue(capacity: Int = 16) {

    private val channel = Channel<TransferRequest>(capacity)

    suspend fun submit(request: TransferRequest): Unit = TODO("m08.4.1")

    fun start(scope: CoroutineScope, handler: suspend (TransferRequest) -> Unit): Job =
        TODO("m08.4.2")

    fun close(): Unit = TODO("m08.4.3")
}
