package ledger.domain

data class Account(
    val id: AccountId,
    val ownerName: String,
    val status: AccountStatus,
    val balance: Money,
    val version: Long = 0,
) {
    init {
        require(ownerName.isNotBlank()) { "ownerName must not be blank" }
        require(version >= 0) { "version must be non-negative" }
    }

    fun freeze(): Account {
        require(status != AccountStatus.CLOSED) { "closed account cannot be frozen" }
        return if (status == AccountStatus.FROZEN) {
            this
        } else {
            copy(status = AccountStatus.FROZEN, version = version + 1)
        }
    }

    fun activate(): Account {
        require(status != AccountStatus.CLOSED) { "closed account cannot be activated" }
        return if (status == AccountStatus.ACTIVE) {
            this
        } else {
            copy(status = AccountStatus.ACTIVE, version = version + 1)
        }
    }

    fun close(): Account {
        require(balance.isZero) { "account with non-zero balance cannot be closed" }
        return if (status == AccountStatus.CLOSED) {
            this
        } else {
            copy(status = AccountStatus.CLOSED, version = version + 1)
        }
    }

    fun withBalance(newBalance: Money): Account {
        require(newBalance.currency == balance.currency) { "currency mismatch" }
        return if (newBalance == balance) {
            this
        } else {
            copy(balance = newBalance, version = version + 1)
        }
    }
}
