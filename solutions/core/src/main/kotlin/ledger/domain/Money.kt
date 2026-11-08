package ledger.domain

data class Money(
    val minor: Long,
    val currency: Currency,
) {
    init {
        require(minor >= 0) { "minor must be non-negative" }
    }

    val isZero: Boolean
        get() = minor == 0L

    companion object {
        fun zero(currency: Currency): Money = Money(0, currency)

        fun ofMajor(major: Long, currency: Currency): Money {
            require(major >= 0) { "major must be non-negative" }
            val factor = when (currency.fractionDigits) {
                0 -> 1L
                2 -> 100L
                else -> error("unsupported fraction digits: ${currency.fractionDigits}")
            }
            return Money(Math.multiplyExact(major, factor), currency)
        }
    }
}
