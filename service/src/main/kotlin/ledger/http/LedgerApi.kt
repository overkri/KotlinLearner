package ledger.http

import kotlinx.serialization.Serializable
import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.TransferRequest
import ledger.domain.TransferResult

interface AccountService {
    suspend fun findById(id: AccountId): Account?
    suspend fun findAll(): List<Account>
}

interface TransferService {
    suspend fun transfer(request: TransferRequest): TransferResult
}

data class LedgerDependencies(
    val accounts: AccountService,
    val transfers: TransferService,
)

@Serializable
data class AccountDto(
    val id: String,
    val ownerName: String,
    val status: String,
    val balanceMinor: Long,
    val currency: String,
)

@Serializable
data class HealthDto(
    val status: String,
)

fun Account.toDto(): AccountDto = AccountDto(
    id = id.value,
    ownerName = ownerName,
    status = status.name,
    balanceMinor = balance.minor,
    currency = balance.currency.name,
)
