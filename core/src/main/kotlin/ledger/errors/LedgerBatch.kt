package ledger.errors

/**
 * Ресурс с явным жизненным циклом. Предоставляется готовым: студент не меняет
 * его, а учится закрывать через `use`.
 */
class LedgerBatch(private val sink: MutableList<String>) : AutoCloseable {

    var closed: Boolean = false
        private set

    fun append(line: String) {
        check(!closed) { "batch is closed" }
        sink += line
    }

    override fun close() {
        closed = true
    }
}
