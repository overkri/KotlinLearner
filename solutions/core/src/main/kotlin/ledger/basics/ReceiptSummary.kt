package ledger.basics

fun receiptSummary(amountsMinor: List<Long>, currency: String): String = buildString {
    var total = 0L
    for (index in amountsMinor.indices) {
        val amount = amountsMinor[index]
        total += amount
        if (index > 0) append('\n')
        append(index + 1).append(". ").append(formatMinor(amount, currency))
    }
    if (amountsMinor.isNotEmpty()) append('\n')
    append("TOTAL: ").append(formatMinor(total, currency))
}
