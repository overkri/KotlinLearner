package ledger.migration

class JvmAnnotationsKata {

    private val pricingEngine = PricingEngine()

    @JvmOverloads
    fun price(quantity: Int, unitPriceCents: Long, tier: String = "STANDARD"): Long =
        pricingEngine.priceCents(quantity, unitPriceCents, tier)

    @Throws(IllegalArgumentException::class)
    fun parseTier(value: String): String {
        val normalized = value.trim().uppercase()
        return when (normalized) {
            "STANDARD", "SILVER", "GOLD" -> normalized
            else -> throw IllegalArgumentException("unknown tier: $value")
        }
    }
}
