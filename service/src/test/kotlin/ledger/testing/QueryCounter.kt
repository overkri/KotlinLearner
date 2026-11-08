package ledger.testing

import java.io.PrintWriter
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy
import java.sql.Connection
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger
import javax.sql.DataSource

/**
 * DataSource-обёртка, считающая prepareStatement/createStatement.
 * Используется для проверки N+1: репозиторий должен сделать один запрос.
 */
class QueryCounter(private val delegate: DataSource) : DataSource {

    val count = AtomicInteger(0)

    fun reset() {
        count.set(0)
    }

    override fun getConnection(): Connection = wrap(delegate.connection)

    override fun getConnection(username: String?, password: String?): Connection =
        wrap(delegate.getConnection(username, password))

    override fun getLogWriter(): PrintWriter = delegate.logWriter

    override fun setLogWriter(out: PrintWriter?) {
        delegate.logWriter = out
    }

    override fun setLoginTimeout(seconds: Int) {
        delegate.loginTimeout = seconds
    }

    override fun getLoginTimeout(): Int = delegate.loginTimeout

    override fun getParentLogger(): Logger = delegate.parentLogger

    override fun <T : Any?> unwrap(iface: Class<T>?): T = delegate.unwrap(iface)

    override fun isWrapperFor(iface: Class<*>?): Boolean = delegate.isWrapperFor(iface)

    private fun wrap(connection: Connection): Connection =
        Proxy.newProxyInstance(
            connection.javaClass.classLoader,
            arrayOf(Connection::class.java),
        ) { _, method, args ->
            if (method.name == "prepareStatement" || method.name == "createStatement") {
                count.incrementAndGet()
            }
            try {
                method.invoke(connection, *(args ?: emptyArray()))
            } catch (error: InvocationTargetException) {
                throw error.targetException
            }
        } as Connection
}
