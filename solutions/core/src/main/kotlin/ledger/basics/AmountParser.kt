package ledger.basics

fun parseAmount(text: String): Long? {
    val trimmed = text.trim()
    if (trimmed.isEmpty() || trimmed.startsWith("+")) return null

    val negative = trimmed.startsWith("-")
    val body = if (negative) trimmed.drop(1) else trimmed
    if (body.isEmpty() || body.any { it == '+' || it == '-' }) return null

    val parts = body.split('.')
    if (parts.size > 2) return null
    if (parts.size == 2 && parts[1].isEmpty()) return null

    val major = parts[0].toLongOrNull() ?: return null
    val fraction = if (parts.size == 2) parts[1] else ""
    if (fraction.length > 2 || fraction.any { !it.isDigit() }) return null

    val cents = (fraction + "00").take(2).toLong()
    val result = major * 100 + cents
    return if (negative) -result else result
}
