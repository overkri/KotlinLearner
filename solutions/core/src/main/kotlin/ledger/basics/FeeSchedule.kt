package ledger.basics

fun feeBps(currency: String, amountMinor: Long): Int {
    require(amountMinor >= 0) { "amount must be non-negative" }
    return when (currency) {
        "USD" -> when {
            amountMinor < 1_000 -> 50
            amountMinor < 10_000 -> 25
            else -> 10
        }
        "EUR" -> if (amountMinor < 5_000) 40 else 20
        "GBP" -> 30
        "JPY" -> 0
        else -> 100
    }
}
