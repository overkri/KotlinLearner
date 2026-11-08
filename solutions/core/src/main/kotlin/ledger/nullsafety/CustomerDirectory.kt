package ledger.nullsafety

import ledger.legacy.LegacyCustomerDirectory

class CustomerDirectory(
    private val legacy: LegacyCustomerDirectory = LegacyCustomerDirectory(),
) {
    fun find(id: String): Customer? {
        val legacyCustomer = legacy.find(id) ?: return null
        return legacyCustomer.toCustomer()
    }

    fun displayName(id: String): String {
        val customer = find(id) ?: return "Unknown customer"
        return customer.name?.takeIf { it.isNotBlank() } ?: customer.id
    }

    fun emailDomain(id: String): String? {
        val email = find(id)?.email ?: return null
        return email.substringAfter('@', missingDelimiterValue = "")
            .takeIf { it.isNotBlank() }
    }
}
