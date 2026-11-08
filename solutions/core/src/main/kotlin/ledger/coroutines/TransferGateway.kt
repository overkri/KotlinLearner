package ledger.coroutines

import ledger.domain.TransferRequest
import ledger.domain.TransferResult

fun interface TransferClient {
    suspend fun submit(request: TransferRequest): TransferResult
}

data class RetryPolicy(
    val attempts: Int = 3,
    val initialDelayMillis: Long = 50,
)

class TransferGateway(
    private val client: TransferClient,
    private val policy: RetryPolicy = RetryPolicy(),
) {
    suspend fun transfer(request: TransferRequest): TransferResult =
        retry(policy.attempts, policy.initialDelayMillis) {
            client.submit(request)
        }
}
