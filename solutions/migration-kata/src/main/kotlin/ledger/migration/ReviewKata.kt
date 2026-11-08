package ledger.migration

import ledger.domain.Transfer
import ledger.domain.TransferStatus

fun reviewTransfers(transfers: List<Transfer>): List<Transfer> =
    transfers
        .filter { it.status == TransferStatus.COMPLETED }
        .groupBy { it.idempotencyKey }
        .values
        .mapNotNull { group ->
            group.maxWithOrNull(
                compareBy<Transfer> { it.createdAt }.thenBy { it.id.value },
            )
        }
        .sortedWith(compareBy({ it.createdAt }, { it.id.value }))
