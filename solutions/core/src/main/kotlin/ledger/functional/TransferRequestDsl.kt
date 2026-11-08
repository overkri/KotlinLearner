package ledger.functional

import ledger.domain.AccountId
import ledger.domain.IdempotencyKey
import ledger.domain.Money
import ledger.domain.TransferRequest

@DslMarker
annotation class TransferDsl

@TransferDsl
class TransferRequestBuilder {
    var from: AccountId? = null
    var to: AccountId? = null
    var amount: Money? = null
    var idempotencyKey: IdempotencyKey? = null
    var note: String? = null

    fun build(): TransferRequest = TransferRequest(
        from = requireNotNull(from) { "from is required" },
        to = requireNotNull(to) { "to is required" },
        amount = requireNotNull(amount) { "amount is required" },
        idempotencyKey = requireNotNull(idempotencyKey) { "idempotencyKey is required" },
        note = note,
    )
}

fun transferRequest(block: TransferRequestBuilder.() -> Unit): TransferRequest =
    TransferRequestBuilder().apply(block).build()
