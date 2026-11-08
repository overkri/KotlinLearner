package ledger.collections

import ledger.domain.LedgerEntry

fun firstAbove(entries: Sequence<LedgerEntry>, thresholdMinor: Long): LedgerEntry? =
    entries.firstOrNull { it.amount.minor > thresholdMinor }
