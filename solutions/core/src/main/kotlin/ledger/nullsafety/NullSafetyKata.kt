package ledger.nullsafety

fun firstNonBlank(vararg values: String?): String? =
    values.firstOrNull { !it.isNullOrBlank() }

fun parsePort(value: String?): Int =
    value?.trim()?.toIntOrNull()?.takeIf { it in 1..65535 } ?: 8080

fun requireEmail(customer: Customer): String =
    customer.email?.takeIf { it.isNotBlank() } ?: error("customer has no email")

fun normalizeName(name: String?): String =
    name?.trim()?.takeIf { it.isNotEmpty() } ?: "anonymous"
