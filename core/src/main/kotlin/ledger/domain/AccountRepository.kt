package ledger.domain

/**
 * Абстракция хранения счетов. Реализации появятся в модуле 06 (in-memory fake)
 * и в модуле 12 (JDBC/Exposed).
 */
interface AccountRepository {
    fun findById(id: AccountId): Account?
    fun save(account: Account)
}
