package ledger.state

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ledger.domain.AccountId
import java.util.concurrent.ConcurrentHashMap

class AccountLockRegistry {

    private val locks = ConcurrentHashMap<AccountId, Mutex>()

    suspend fun <T> withLock(id: AccountId, block: suspend () -> T): T =
        locks.computeIfAbsent(id) { Mutex() }.withLock { block() }
}
