package ledger.domain

enum class AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED,
    ;

    val isTerminal: Boolean
        get() = this == CLOSED

    fun canTransitionTo(target: AccountStatus): Boolean = when (this) {
        ACTIVE -> target == FROZEN || target == CLOSED
        FROZEN -> target == ACTIVE || target == CLOSED
        CLOSED -> false
    }
}
