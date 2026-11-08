package ledger.domain

import java.time.Instant

enum class TransferStatus {
    PENDING,
    COMPLETED,
    REJECTED,
}

/**
 * Заявка на перевод. Data-only контракт: студент его не меняет.
 * Создаётся вручную или через DSL из модуля 05.
 */
data class TransferRequest(
    val from: AccountId,
    val to: AccountId,
    val amount: Money,
    val idempotencyKey: IdempotencyKey,
    val note: String? = null,
)

/** Сохранённый перевод. */
data class Transfer(
    val id: TransferId,
    val from: AccountId,
    val to: AccountId,
    val amount: Money,
    val status: TransferStatus,
    val idempotencyKey: IdempotencyKey,
    val createdAt: Instant,
)

/** Результат бизнес-операции: успех или доменная ошибка, без исключений. */
sealed interface TransferResult {
    data class Success(val transfer: Transfer) : TransferResult
    data class Failure(val error: TransferError) : TransferResult
}
