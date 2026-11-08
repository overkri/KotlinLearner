package ledger.persistence

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp

/**
 * Exposed-схема, совпадающая с Flyway-миграцией `V1__init.sql`.
 * Таблицы предоставлены готовыми: упражнения — про репозитории и транзакции.
 */
object AccountsTable : Table("accounts") {
    val id = varchar("id", 64)
    val ownerName = varchar("owner_name", 255)
    val status = varchar("status", 16)
    val balanceMinor = long("balance_minor")
    val currency = varchar("currency", 8)
    val version = long("version")

    override val primaryKey = PrimaryKey(id)
}

object LedgerEntriesTable : Table("ledger_entries") {
    val id = long("id")
    val transferId = varchar("transfer_id", 64)
    val accountId = varchar("account_id", 64).index()
    val counterparty = varchar("counterparty", 64)
    val direction = varchar("direction", 16)
    val amountMinor = long("amount_minor")
    val currency = varchar("currency", 8)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}
