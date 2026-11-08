package ledger.errors

import ledger.domain.AccountRepository
import ledger.domain.AccountStatus
import ledger.domain.Transfer
import ledger.domain.TransferError
import ledger.domain.TransferId
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.domain.TransferStatus
import ledger.functional.minus
import ledger.functional.plus
import java.time.Instant
import java.util.UUID

class TransferEngine(
    private val accounts: AccountRepository,
    private val nextTransferId: () -> TransferId = { TransferId("tr-${UUID.randomUUID()}") },
    private val now: () -> Instant = { Instant.now() },
) {
    fun transfer(request: TransferRequest): TransferResult {
        val amount = request.amount
        if (amount.minor <= 0) {
            return TransferResult.Failure(TransferError.InvalidAmount(amount))
        }
        if (request.from == request.to) {
            return TransferResult.Failure(TransferError.SameAccount)
        }

        val source = accounts.findById(request.from)
            ?: return TransferResult.Failure(TransferError.AccountNotFound(request.from))
        val target = accounts.findById(request.to)
            ?: return TransferResult.Failure(TransferError.AccountNotFound(request.to))

        if (source.status != AccountStatus.ACTIVE) {
            return TransferResult.Failure(TransferError.AccountFrozen(source.id))
        }
        if (target.status != AccountStatus.ACTIVE) {
            return TransferResult.Failure(TransferError.AccountFrozen(target.id))
        }
        if (source.balance.currency != amount.currency) {
            return TransferResult.Failure(
                TransferError.CurrencyMismatch(source.balance.currency, amount.currency),
            )
        }
        if (target.balance.currency != amount.currency) {
            return TransferResult.Failure(
                TransferError.CurrencyMismatch(target.balance.currency, amount.currency),
            )
        }
        if (source.balance.minor < amount.minor) {
            return TransferResult.Failure(
                TransferError.InsufficientFunds(source.id, source.balance, amount),
            )
        }

        val transfer = Transfer(
            id = nextTransferId(),
            from = request.from,
            to = request.to,
            amount = amount,
            status = TransferStatus.COMPLETED,
            idempotencyKey = request.idempotencyKey,
            createdAt = now(),
        )

        accounts.save(source.withBalance(source.balance - amount))
        accounts.save(target.withBalance(target.balance + amount))
        return TransferResult.Success(transfer)
    }
}
