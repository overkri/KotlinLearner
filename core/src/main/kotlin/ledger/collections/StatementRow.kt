package ledger.collections

import ledger.domain.Direction
import ledger.domain.Money
import ledger.domain.TransferId

/**
 * Строка выписки. `balanceAfterMinor` — знаковый остаток в минорных единицах:
 * DEBIT уменьшает его, CREDIT увеличивает. Предоставляется готовым.
 */
data class StatementRow(
    val index: Int,
    val entryId: Long,
    val transferId: TransferId,
    val direction: Direction,
    val amount: Money,
    val balanceAfterMinor: Long,
)
