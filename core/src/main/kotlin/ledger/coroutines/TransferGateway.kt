package ledger.coroutines

import ledger.domain.TransferRequest
import ledger.domain.TransferResult

/** Клиент внешнего transfer-сервиса. */
fun interface TransferClient {
    suspend fun submit(request: TransferRequest): TransferResult
}

data class RetryPolicy(
    val attempts: Int = 3,
    val initialDelayMillis: Long = 50,
)

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 7 · УПРАЖНЕНИЕ 7.2 — Шлюз с ретраями
 * ═══════════════════════════════════════════════════════════
 *  Тема:           suspend-функции, retry, бизнес-ошибки vs временные сбои
 *  Java-аналогия:  @Retryable из Spring Retry + Feign-клиент
 *  Тесты:          ./gradlew :core:test -Pmodule=07 --tests "ledger.m07.TransferGatewayTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Оберните TransferClient ретраями. Важно различать временный сбой
 *  (RetryableException) и бизнес-отказ (TransferResult.Failure): второй
 *  повторять нельзя.
 *
 *  ТРЕБОВАНИЯ
 *  1. transfer(request) вызывает client.submit(request) через retry(...)
 *     с policy.attempts и policy.initialDelayMillis.
 *  2. TransferResult.Failure возвращается сразу, без повторов.
 *  3. RetryableException повторяется; другие исключения пробрасываются сразу.
 *  4. Успешный результат возвращается без изменений.
 *
 *  ПОДСКАЗКИ
 *  - `retry(policy.attempts, policy.initialDelayMillis) { client.submit(request) }`.
 *  - retry по контракту не глотает не-RetryableException.
 *  - Бизнес-отказ — это успешный возврат Failure, а не исключение.
 *
 *  ЛОВУШКА
 *  Почему повтор перевода без идемпотентности опасен? Как idempotencyKey
 *  защищает от двойного списания? (Подробнее — модуль 13.)
 * ═══════════════════════════════════════════════════════════
 */
class TransferGateway(
    private val client: TransferClient,
    private val policy: RetryPolicy = RetryPolicy(),
) {
    suspend fun transfer(request: TransferRequest): TransferResult = TODO("m07.2.1")
}
