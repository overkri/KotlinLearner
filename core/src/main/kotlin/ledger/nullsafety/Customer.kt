package ledger.nullsafety

import ledger.legacy.LegacyCustomer

/**
 * Kotlin-представление клиента. Все «необязательные» поля честно помечены `?`.
 * Это контракт, который студент не меняет.
 */
data class Customer(
    val id: String,
    val name: String?,
    val email: String?,
    val nickname: String?,
)

/** Единственное место, где platform types из Java превращаются в Kotlin-типы. */
internal fun LegacyCustomer.toCustomer(): Customer = Customer(
    id = id,
    name = name,
    email = email,
    nickname = nickname,
)
