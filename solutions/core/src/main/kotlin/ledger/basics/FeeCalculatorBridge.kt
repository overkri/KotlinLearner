package ledger.basics

import ledger.legacy.LegacyFeeCalculator

class FeeCalculatorBridge(
    private val legacy: LegacyFeeCalculator = LegacyFeeCalculator(),
) {
    fun feeFor(amountMinor: Long, currency: String = "USD"): Long =
        legacy.feeFor(amountMinor, currency)

    fun feeForUsd(amountMinor: Long): Long = legacy.feeFor(amountMinor)

    fun describe(currency: String): String =
        LegacyFeeCalculator.describe(currency) ?: "Unknown currency"
}
