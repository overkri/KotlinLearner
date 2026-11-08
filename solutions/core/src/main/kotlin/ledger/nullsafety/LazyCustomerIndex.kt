package ledger.nullsafety

import ledger.legacy.LegacyCustomerDirectory

class LazyCustomerIndex(
    private val legacy: LegacyCustomerDirectory = LegacyCustomerDirectory(),
) {
    var indexBuilds: Int = 0
        private set

    val all: List<Customer> by lazy {
        indexBuilds += 1
        buildList {
            for (legacyCustomer in legacy.all().values) {
                add(legacyCustomer.toCustomer())
            }
        }
    }

    fun find(id: String): Customer? = all.firstOrNull { it.id == id }
}
