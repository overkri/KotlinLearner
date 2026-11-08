package ledger.persistence

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class TxRunner(
    private val database: Database,
) {
    suspend fun <T> tx(block: suspend JdbcTransaction.() -> T): T =
        suspendTransaction(database) {
            block()
        }
}
