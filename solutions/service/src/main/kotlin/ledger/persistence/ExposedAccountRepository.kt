package ledger.persistence

import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountRepository
import ledger.domain.AccountStatus
import ledger.domain.Currency
import ledger.domain.Money
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class ExposedAccountRepository(
    private val database: Database,
) : AccountRepository {

    override fun findById(id: AccountId): Account? = transaction(database) {
        AccountsTable.selectAll()
            .where { AccountsTable.id eq id.value }
            .singleOrNull()
            ?.toAccount()
    }

    override fun save(account: Account) {
        transaction(database) {
            if (account.version == 0L) {
                AccountsTable.insert {
                    it[id] = account.id.value
                    it[ownerName] = account.ownerName
                    it[status] = account.status.name
                    it[balanceMinor] = account.balance.minor
                    it[currency] = account.balance.currency.name
                    it[version] = account.version
                }
            } else {
                val updated = AccountsTable.update({
                    (AccountsTable.id eq account.id.value) and
                        (AccountsTable.version eq account.version - 1)
                }) {
                    it[ownerName] = account.ownerName
                    it[status] = account.status.name
                    it[balanceMinor] = account.balance.minor
                    it[currency] = account.balance.currency.name
                    it[version] = account.version
                }
                if (updated != 1) {
                    throw OptimisticLockException(
                        "optimistic lock failed for account ${account.id.value}",
                    )
                }
            }
        }
    }

    private fun ResultRow.toAccount(): Account = Account(
        id = AccountId(this[AccountsTable.id]),
        ownerName = this[AccountsTable.ownerName],
        status = AccountStatus.valueOf(this[AccountsTable.status]),
        balance = Money(
            this[AccountsTable.balanceMinor],
            Currency.valueOf(this[AccountsTable.currency]),
        ),
        version = this[AccountsTable.version],
    )
}
