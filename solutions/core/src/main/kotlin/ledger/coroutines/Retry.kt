package ledger.coroutines

import kotlinx.coroutines.delay

class RetryableException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

suspend fun <T> retry(
    times: Int,
    initialDelayMillis: Long,
    block: suspend () -> T,
): T {
    require(times >= 1) { "times must be at least 1" }
    require(initialDelayMillis >= 0) { "initialDelayMillis must be non-negative" }

    var delayMillis = initialDelayMillis
    var lastError: RetryableException? = null

    repeat(times) { attempt ->
        try {
            return block()
        } catch (error: RetryableException) {
            lastError = error
            if (attempt < times - 1) {
                delay(delayMillis)
                delayMillis *= 2
            }
        }
    }

    throw lastError ?: IllegalStateException("retry loop finished without a result")
}
