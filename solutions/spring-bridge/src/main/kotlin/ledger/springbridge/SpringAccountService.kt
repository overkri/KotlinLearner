package ledger.springbridge

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SpringAccountService(
    private val repository: AccountJpaRepository,
) {
    @Transactional(readOnly = true)
    fun find(id: String): AccountEntity? = repository.findById(id).orElse(null)

    @Transactional
    fun freeze(id: String): AccountEntity? {
        val account = repository.findById(id).orElse(null) ?: return null
        if (account.status == "CLOSED") {
            throw IllegalArgumentException("closed account cannot be frozen")
        }
        if (account.status != "FROZEN") {
            account.status = "FROZEN"
            account.version += 1
            repository.save(account)
        }
        return account
    }
}
