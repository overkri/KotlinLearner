package ledger.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

suspend fun <T> runAllSettled(tasks: List<suspend () -> T>): List<Result<T>> =
    supervisorScope {
        tasks.map { task ->
            async {
                try {
                    Result.success(task())
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (error: Exception) {
                    Result.failure(error)
                }
            }
        }.awaitAll()
    }
