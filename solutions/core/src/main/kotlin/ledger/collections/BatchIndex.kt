package ledger.collections

import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountStatus

fun chunkIds(ids: List<AccountId>, size: Int): List<List<AccountId>> {
    require(size > 0) { "size must be positive" }
    return ids.chunked(size)
}

fun indexById(accounts: List<Account>): Map<AccountId, Account> =
    accounts.associateBy { it.id }

fun partitionByStatus(accounts: List<Account>): Pair<List<Account>, List<Account>> =
    accounts.partition { it.status == AccountStatus.ACTIVE }
