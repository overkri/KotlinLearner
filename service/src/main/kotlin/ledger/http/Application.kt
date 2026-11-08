package ledger.http

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val ledger = InMemoryLedger()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        ledgerModule(LedgerDependencies(accounts = ledger, transfers = ledger))
    }.start(wait = true)
}
