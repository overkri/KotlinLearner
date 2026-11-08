package ledger.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ledger.domain.AccountId
import ledger.domain.DomainEvent

class BalanceProjector(
    initial: Map<AccountId, Long> = emptyMap(),
) {
    private val mutableBalances = MutableStateFlow(initial)

    val balances: StateFlow<Map<AccountId, Long>> = mutableBalances.asStateFlow()

    fun apply(event: DomainEvent) {
        when (event) {
            is DomainEvent.TransferCompleted -> mutableBalances.update { balances ->
                val fromBalance = (balances[event.from] ?: 0L) - event.amount.minor
                val toBalance = (balances[event.to] ?: 0L) + event.amount.minor
                balances + (event.from to fromBalance) + (event.to to toBalance)
            }
            is DomainEvent.TransferRejected -> Unit
            is DomainEvent.AccountFrozen -> Unit
        }
    }
}
