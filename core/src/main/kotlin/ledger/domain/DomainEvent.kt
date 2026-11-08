package ledger.domain

import java.time.Instant

/**
 * Доменные события. `sealed` позволяет обрабатывать их исчерпывающим `when`.
 * Data-only контракт; события публикуются в модулях 08 и 13.
 */
sealed interface DomainEvent {
    val occurredAt: Instant

    data class TransferCompleted(
        val transferId: TransferId,
        val from: AccountId,
        val to: AccountId,
        val amount: Money,
        override val occurredAt: Instant,
    ) : DomainEvent

    data class TransferRejected(
        val transferId: TransferId,
        val from: AccountId,
        val to: AccountId,
        val error: TransferError,
        override val occurredAt: Instant,
    ) : DomainEvent

    data class AccountFrozen(
        val accountId: AccountId,
        override val occurredAt: Instant,
    ) : DomainEvent
}
