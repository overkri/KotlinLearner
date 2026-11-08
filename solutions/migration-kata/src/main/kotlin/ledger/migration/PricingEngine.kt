package ledger.migration

class PricingEngine {
    fun priceCents(quantity: Int, unitPriceCents: Long, tier: String): Long {
        require(quantity > 0) { "quantity must be positive" }
        val subtotal = Math.multiplyExact(quantity.toLong(), unitPriceCents)
        return when (tier) {
            "STANDARD" -> subtotal
            "SILVER" -> subtotal * 95 / 100
            "GOLD" -> subtotal * 90 / 100
            else -> throw IllegalArgumentException("unknown tier: $tier")
        }
    }
}
