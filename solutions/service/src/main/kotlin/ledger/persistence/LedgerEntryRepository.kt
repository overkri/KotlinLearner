package ledger.persistence

import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.Direction
import ledger.domain.LedgerEntry
import ledger.domain.Money
import ledger.domain.TransferId
import java.sql.ResultSet
import javax.sql.DataSource

class JdbcLedgerEntryRepository(
    private val dataSource: DataSource,
) {
    fun findByAccountIds(accountIds: List<AccountId>): Map<AccountId, List<LedgerEntry>> {
        if (accountIds.isEmpty()) return emptyMap()

        val ids = accountIds.distinct()
        val placeholders = ids.joinToString(",") { "?" }
        val grouped = ids.associateWith { mutableListOf<LedgerEntry>() }

        dataSource.connection.use { connection ->
            connection.prepareStatement(
                """
                SELECT id, transfer_id, account_id, counterparty, direction,
                       amount_minor, currency, created_at
                FROM ledger_entries
                WHERE account_id IN ($placeholders)
                ORDER BY id
                """.trimIndent(),
            ).use { statement ->
                ids.forEachIndexed { index, id ->
                    statement.setString(index + 1, id.value)
                }
                statement.executeQuery().use { result ->
                    while (result.next()) {
                        val entry = result.toEntry()
                        grouped.getValue(entry.accountId).add(entry)
                    }
                }
            }
        }

        return grouped.mapValues { (_, entries) -> entries.toList() }
    }

    private fun ResultSet.toEntry(): LedgerEntry = LedgerEntry(
        id = getLong("id"),
        transferId = TransferId(getString("transfer_id")),
        accountId = AccountId(getString("account_id")),
        counterparty = AccountId(getString("counterparty")),
        direction = Direction.valueOf(getString("direction")),
        amount = Money(getLong("amount_minor"), Currency.valueOf(getString("currency"))),
        createdAt = getTimestamp("created_at").toInstant(),
    )
}
