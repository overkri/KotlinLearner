package ledger.http

import io.ktor.http.HttpStatusCode
import ledger.domain.TransferError

fun TransferError.toHttpStatus(): HttpStatusCode = when (this) {
    is TransferError.AccountNotFound -> HttpStatusCode.NotFound
    is TransferError.AccountFrozen -> HttpStatusCode.Conflict
    is TransferError.InsufficientFunds -> HttpStatusCode.Conflict
    is TransferError.CurrencyMismatch -> HttpStatusCode.UnprocessableEntity
    is TransferError.InvalidAmount -> HttpStatusCode.BadRequest
    TransferError.SameAccount -> HttpStatusCode.BadRequest
}
