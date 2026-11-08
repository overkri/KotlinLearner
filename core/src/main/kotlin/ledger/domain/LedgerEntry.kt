package ledger.domain

import java.time.Instant

enum class Direction {
    DEBIT,
    CREDIT,
}

/**
 * Проводка по счёту. Двойная запись: у перевода две проводки — DEBIT и CREDIT.
 * Data-only контракт.
 */
data class LedgerEntry(
    val id: Long,
    val transferId: TransferId,
    val accountId: AccountId,
    val counterparty: AccountId,
    val direction: Direction,
    val amount: Money,
    val createdAt: Instant,
)
