package ledger.functional

import ledger.domain.Money
import kotlin.properties.Delegates

class TransferLimits(initialMaxMinor: Long) {
    var maxMinor: Long by Delegates.vetoable(initialMaxMinor) { _, _, newValue ->
        newValue in 1..1_000_000
    }

    fun allows(amount: Money): Boolean = amount.minor <= maxMinor
}
