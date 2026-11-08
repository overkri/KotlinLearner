package ledger.collections

import ledger.domain.AccountId
import ledger.domain.LedgerEntry

fun statement(entries: List<LedgerEntry>, accountId: AccountId): List<StatementRow> {
    val forAccount = entries.filter { it.accountId == accountId }
    val currencies = forAccount.map { it.amount.currency }.distinct()
    require(currencies.size <= 1) { "mixed currencies in statement" }

    var balance = 0L
    return forAccount
        .sortedWith(compareBy({ it.createdAt }, { it.id }))
        .mapIndexed { index, entry ->
            balance += signedMinor(entry)
            StatementRow(
                index = index + 1,
                entryId = entry.id,
                transferId = entry.transferId,
                direction = entry.direction,
                amount = entry.amount,
                balanceAfterMinor = balance,
            )
        }
}
