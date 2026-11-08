package ledger.persistence

import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountRepository
import ledger.domain.AccountStatus
import ledger.domain.Currency
import ledger.domain.Money
import java.sql.ResultSet
import javax.sql.DataSource

class JdbcAccountRepository(
    private val dataSource: DataSource,
) : AccountRepository {

    override fun findById(id: AccountId): Account? =
        dataSource.connection.use { connection ->
            connection.prepareStatement(
                """
                SELECT id, owner_name, status, balance_minor, currency, version
                FROM accounts
                WHERE id = ?
                """.trimIndent(),
            ).use { statement ->
                statement.setString(1, id.value)
                statement.executeQuery().use { result ->
                    if (result.next()) result.toAccount() else null
                }
            }
        }

    override fun save(account: Account) {
        dataSource.connection.use { connection ->
            if (account.version == 0L) {
                connection.prepareStatement(
                    """
                    INSERT INTO accounts (id, owner_name, status, balance_minor, currency, version)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """.trimIndent(),
                ).use { statement ->
                    statement.setString(1, account.id.value)
                    statement.setString(2, account.ownerName)
                    statement.setString(3, account.status.name)
                    statement.setLong(4, account.balance.minor)
                    statement.setString(5, account.balance.currency.name)
                    statement.setLong(6, account.version)
                    statement.executeUpdate()
                }
            } else {
                connection.prepareStatement(
                    """
                    UPDATE accounts
                    SET owner_name = ?, status = ?, balance_minor = ?, currency = ?, version = ?
                    WHERE id = ? AND version = ?
                    """.trimIndent(),
                ).use { statement ->
                    statement.setString(1, account.ownerName)
                    statement.setString(2, account.status.name)
                    statement.setLong(3, account.balance.minor)
                    statement.setString(4, account.balance.currency.name)
                    statement.setLong(5, account.version)
                    statement.setString(6, account.id.value)
                    statement.setLong(7, account.version - 1)

                    val updated = statement.executeUpdate()
                    if (updated != 1) {
                        throw OptimisticLockException(
                            "optimistic lock failed for account ${account.id.value}",
                        )
                    }
                }
            }
        }
    }

    private fun ResultSet.toAccount(): Account = Account(
        id = AccountId(getString("id")),
        ownerName = getString("owner_name"),
        status = AccountStatus.valueOf(getString("status")),
        balance = Money(getLong("balance_minor"), Currency.valueOf(getString("currency"))),
        version = getLong("version"),
    )
}
