package ledger.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ledger.domain.Account
import ledger.domain.AccountId

suspend fun loadAccounts(
    ids: List<AccountId>,
    fetcher: suspend (AccountId) -> Account,
): List<Account> = coroutineScope {
    ids.map { id -> async { fetcher(id) } }.awaitAll()
}
