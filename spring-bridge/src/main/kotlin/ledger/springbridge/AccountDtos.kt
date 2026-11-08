package ledger.springbridge

data class AccountResponse(
    val id: String,
    val ownerName: String,
    val status: String,
    val balanceMinor: Long,
    val currency: String,
    val version: Long,
)

data class ErrorResponse(
    val code: String,
    val message: String,
)

fun AccountEntity.toResponse(): AccountResponse = AccountResponse(
    id = id,
    ownerName = ownerName,
    status = status,
    balanceMinor = balanceMinor,
    currency = currency,
    version = version,
)

class AccountNotFoundException(accountId: String) :
    RuntimeException("Account not found: $accountId")
