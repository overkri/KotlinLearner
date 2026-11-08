package ledger.functional

import ledger.domain.Account
import ledger.domain.AccountStatus
import ledger.domain.Transfer

fun auditLine(transfer: Transfer): String = with(transfer) {
    "transfer=${id.value} from=${from.value} to=${to.value} " +
        "amount=${amount.minor} ${amount.currency} status=$status"
}

fun requireActive(account: Account): Account =
    account.takeIf { it.status == AccountStatus.ACTIVE }
        ?: throw IllegalArgumentException("account is not active")

fun describeOrNull(account: Account?): String? =
    account?.let { "${it.id.value}:${it.balance.minor}" }
