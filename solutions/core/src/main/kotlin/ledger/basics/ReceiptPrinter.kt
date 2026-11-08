package ledger.basics

import java.time.Instant

fun interface Clock {
    fun now(): Instant
}

class ReceiptPrinter(private val clock: Clock) {
    fun print(amountMinor: Long, currency: String): String {
        require(amountMinor > 0) { "amount must be positive" }
        return "$amountMinor $currency @ ${clock.now()}"
    }
}
