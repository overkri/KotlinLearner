package ledger.errors

fun <T> withLedgerBatch(batch: LedgerBatch, block: (LedgerBatch) -> T): T =
    batch.use(block)
