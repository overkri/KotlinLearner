package ledger.functional

import ledger.domain.Money

operator fun Money.plus(other: Money): Money {
    require(currency == other.currency) { "currency mismatch" }
    return Money(Math.addExact(minor, other.minor), currency)
}

operator fun Money.minus(other: Money): Money {
    require(currency == other.currency) { "currency mismatch" }
    require(minor >= other.minor) { "result must be non-negative" }
    return Money(minor - other.minor, currency)
}

operator fun Money.times(factor: Int): Money {
    require(factor >= 0) { "factor must be non-negative" }
    return Money(Math.multiplyExact(minor, factor.toLong()), currency)
}

operator fun Money.compareTo(other: Money): Int {
    require(currency == other.currency) { "currency mismatch" }
    return minor.compareTo(other.minor)
}

fun Iterable<Money>.total(): Money {
    val iterator = iterator()
    require(iterator.hasNext()) { "cannot total an empty collection" }

    val first = iterator.next()
    var sum = first.minor
    while (iterator.hasNext()) {
        val next = iterator.next()
        require(first.currency == next.currency) { "currency mismatch" }
        sum = Math.addExact(sum, next.minor)
    }
    return Money(sum, first.currency)
}
