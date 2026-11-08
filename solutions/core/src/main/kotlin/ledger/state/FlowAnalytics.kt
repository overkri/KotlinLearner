package ledger.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.runningFold
import ledger.collections.signedMinor
import ledger.domain.Currency
import ledger.domain.LedgerEntry
import ledger.domain.Transfer
import ledger.domain.TransferStatus

fun Flow<LedgerEntry>.totalsByCurrencyFlow(): Flow<Map<Currency, Long>> =
    runningFold(emptyMap()) { totals, entry ->
        val currency = entry.amount.currency
        totals + (currency to (totals[currency] ?: 0L) + signedMinor(entry))
    }

fun Flow<Transfer>.completedOnly(): Flow<Transfer> =
    filter { it.status == TransferStatus.COMPLETED }
