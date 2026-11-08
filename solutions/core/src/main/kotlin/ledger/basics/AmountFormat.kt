package ledger.basics

import kotlin.math.abs

fun formatMinor(minor: Long, currency: String): String {
    val sign = if (minor < 0) "-" else ""
    val absolute = abs(minor)
    return when (currency) {
        "USD" -> "${sign}\$${major(absolute)}.${fraction(absolute)}"
        "EUR" -> "${sign}€${major(absolute)}.${fraction(absolute)}"
        "GBP" -> "${sign}£${major(absolute)}.${fraction(absolute)}"
        "JPY" -> "$sign¥$absolute"
        else -> "$sign${major(absolute)}.${fraction(absolute)} $currency"
    }
}

private fun major(minor: Long): Long = minor / 100

private fun fraction(minor: Long): String = (minor % 100).toString().padStart(2, '0')
