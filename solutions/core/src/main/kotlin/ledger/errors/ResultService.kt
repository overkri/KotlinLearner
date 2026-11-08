package ledger.errors

import ledger.basics.parseAmount
import ledger.domain.Account
import ledger.domain.AccountStatus
import ledger.domain.Money
import ledger.functional.minus

fun withdraw(account: Account, amount: Money): Result<Account> {
    if (amount.minor <= 0) {
        return Result.failure(IllegalArgumentException("amount must be positive"))
    }
    if (account.status != AccountStatus.ACTIVE) {
        return Result.failure(IllegalStateException("account is not active"))
    }
    if (amount.currency != account.balance.currency) {
        return Result.failure(IllegalArgumentException("currency mismatch"))
    }
    if (amount.minor > account.balance.minor) {
        return Result.failure(IllegalArgumentException("insufficient funds"))
    }
    return Result.success(account.withBalance(account.balance - amount))
}

fun parseAndWithdraw(account: Account, text: String): Result<Account> {
    val minor = parseAmount(text)
        ?: return Result.failure(IllegalArgumentException("invalid amount"))
    return withdraw(account, Money(minor, account.balance.currency))
}
