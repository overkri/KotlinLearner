package ledger.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountStatus
import ledger.domain.Currency
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.errors.TransferEngine
import ledger.testing.InMemoryAccountRepository
import ledger.testing.TestData

/**
 * Демонстрационный in-memory слой для `main`. Предоставляется готовым:
 * HTTP-модуль не должен требовать базы данных.
 */
class InMemoryLedger : AccountService, TransferService {

    private val repository = InMemoryAccountRepository(
        listOf(
            Account(
                id = AccountId("acc-1"),
                ownerName = "Alice",
                status = AccountStatus.ACTIVE,
                balance = TestData.usd(100_00),
            ),
            Account(
                id = AccountId("acc-2"),
                ownerName = "Bob",
                status = AccountStatus.ACTIVE,
                balance = TestData.usd(50_00),
            ),
        ),
    )

    private val engine = TransferEngine(repository)

    override suspend fun findById(id: AccountId): Account? = repository.findById(id)

    override suspend fun findAll(): List<Account> = repository.snapshot()

    override suspend fun transfer(request: TransferRequest): TransferResult =
        withContext(Dispatchers.Default) {
            engine.transfer(request)
        }
}
