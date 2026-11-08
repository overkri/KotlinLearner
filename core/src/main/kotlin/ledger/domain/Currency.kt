package ledger.domain

/**
 * Валюты курса. `fractionDigits` — количество минорных единиц в одной major-единице:
 * USD/EUR/GBP — центы (2), JPY — иены без дробной части (0).
 */
enum class Currency(val fractionDigits: Int, val symbol: String) {
    USD(2, "$"),
    EUR(2, "€"),
    GBP(2, "£"),
    JPY(0, "¥"),
}
