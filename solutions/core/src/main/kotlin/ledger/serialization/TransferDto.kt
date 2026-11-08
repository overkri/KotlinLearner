package ledger.serialization

import kotlinx.serialization.Serializable
import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.IdempotencyKey
import ledger.domain.Money
import ledger.domain.Transfer
import ledger.domain.TransferError
import ledger.domain.TransferRequest

@Serializable
data class TransferRequestDto(
    val from: String,
    val to: String,
    val amountMinor: Long,
    val currency: String,
    val idempotencyKey: String,
    val note: String? = null,
)

@Serializable
data class TransferResponseDto(
    val id: String,
    val from: String,
    val to: String,
    val amountMinor: Long,
    val currency: String,
    val status: String,
    val idempotencyKey: String,
)

@Serializable
data class ErrorDto(
    val code: String,
    val message: String,
)

fun TransferRequestDto.toDomain(): Result<TransferRequest> = runCatching {
    val resolvedCurrency = Currency.entries.firstOrNull { it.name == currency }
        ?: throw IllegalArgumentException("invalid currency: $currency")
    TransferRequest(
        from = AccountId(from),
        to = AccountId(to),
        amount = Money(amountMinor, resolvedCurrency),
        idempotencyKey = IdempotencyKey(idempotencyKey),
        note = note,
    )
}

fun Transfer.toDto(): TransferResponseDto = TransferResponseDto(
    id = id.value,
    from = from.value,
    to = to.value,
    amountMinor = amount.minor,
    currency = amount.currency.name,
    status = status.name,
    idempotencyKey = idempotencyKey.value,
)

fun TransferError.toErrorDto(): ErrorDto = ErrorDto(
    code = when (this) {
        is TransferError.AccountNotFound -> "account_not_found"
        is TransferError.AccountFrozen -> "account_frozen"
        is TransferError.InsufficientFunds -> "insufficient_funds"
        is TransferError.CurrencyMismatch -> "currency_mismatch"
        is TransferError.InvalidAmount -> "invalid_amount"
        TransferError.SameAccount -> "same_account"
    },
    message = message,
)
