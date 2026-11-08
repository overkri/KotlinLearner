package ledger.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import ledger.domain.TransferRequest

class TransferQueue(capacity: Int = 16) {

    private val channel = Channel<TransferRequest>(capacity)

    suspend fun submit(request: TransferRequest) {
        channel.send(request)
    }

    fun start(scope: CoroutineScope, handler: suspend (TransferRequest) -> Unit): Job =
        scope.launch {
            for (request in channel) {
                handler(request)
            }
        }

    fun close() {
        channel.close()
    }
}
