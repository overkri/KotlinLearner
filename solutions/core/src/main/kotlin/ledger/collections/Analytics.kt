package ledger.collections

import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.Direction
import ledger.domain.LedgerEntry
import ledger.domain.Money

fun totalsByCurrency(entries: List<LedgerEntry>): Map<Currency, Long> =
    entries.groupBy { it.amount.currency }
        .mapValues { (_, group) -> group.sumOf(::signedMinor) }

fun busiestAccounts(entries: List<LedgerEntry>, limit: Int): List<Pair<AccountId, Int>> {
    require(limit >= 0) { "limit must be non-negative" }
    return entries.groupingBy { it.accountId }
        .eachCount()
        .toList()
        .sortedWith(
            compareByDescending<Pair<AccountId, Int>> { it.second }
                .thenBy { it.first.value },
        )
        .take(limit)
}

fun averageDebit(entries: List<LedgerEntry>): Money? {
    val debits = entries.filter { it.direction == Direction.DEBIT }
    if (debits.isEmpty()) return null

    val currencies = debits.map { it.amount.currency }.distinct()
    require(currencies.size == 1) { "mixed currencies" }

    val average = debits.sumOf { it.amount.minor } / debits.size
    return Money(average, currencies.single())
}

internal fun signedMinor(entry: LedgerEntry): Long =
    if (entry.direction == Direction.DEBIT) -entry.amount.minor else entry.amount.minor
