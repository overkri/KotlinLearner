package ledger.testing

import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountRepository

/**
 * Простой in-memory репозиторий для тестов модулей 06–08. Предоставляется готовым.
 * Не потокобезопасен: конкурентность будет отдельной темой модуля 08.
 */
class InMemoryAccountRepository(initial: List<Account> = emptyList()) : AccountRepository {

    private val accounts = LinkedHashMap<AccountId, Account>()

    var saves: Int = 0
        private set

    init {
        initial.forEach { accounts[it.id] = it }
    }

    override fun findById(id: AccountId): Account? = accounts[id]

    override fun save(account: Account) {
        saves += 1
        accounts[account.id] = account
    }

    fun snapshot(): List<Account> = accounts.values.toList()
}
