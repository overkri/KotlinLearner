package ledger.domain

sealed interface TransferError {
    val message: String

    data class AccountNotFound(val accountId: AccountId) : TransferError {
        override val message: String = "Account not found: ${accountId.value}"
    }

    data class AccountFrozen(val accountId: AccountId) : TransferError {
        override val message: String = "Account is frozen: ${accountId.value}"
    }

    data class InsufficientFunds(
        val accountId: AccountId,
        val available: Money,
        val requested: Money,
    ) : TransferError {
        override val message: String =
            "Insufficient funds: available ${available.minor} ${available.currency}, " +
                "requested ${requested.minor} ${requested.currency}"
    }

    data class CurrencyMismatch(val from: Currency, val to: Currency) : TransferError {
        override val message: String = "Currency mismatch: ${from.name} -> ${to.name}"
    }

    data class InvalidAmount(val amount: Money) : TransferError {
        override val message: String = "Invalid amount: ${amount.minor} ${amount.currency}"
    }

    data object SameAccount : TransferError {
        override val message: String = "Cannot transfer to the same account"
    }
}
