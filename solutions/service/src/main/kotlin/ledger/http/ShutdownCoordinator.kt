package ledger.http

import java.util.concurrent.atomic.AtomicBoolean

class ShutdownCoordinator {

    private val stopping = AtomicBoolean(false)

    fun beginShutdown() {
        stopping.set(true)
    }

    fun isReady(): Boolean = !stopping.get()
}
