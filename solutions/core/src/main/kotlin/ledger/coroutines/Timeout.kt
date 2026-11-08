package ledger.coroutines

import kotlinx.coroutines.withTimeoutOrNull

suspend fun <T> withTimeoutFallback(
    timeoutMillis: Long,
    fallback: T,
    block: suspend () -> T,
): T {
    require(timeoutMillis > 0) { "timeoutMillis must be positive" }
    return withTimeoutOrNull(timeoutMillis) { block() } ?: fallback
}
