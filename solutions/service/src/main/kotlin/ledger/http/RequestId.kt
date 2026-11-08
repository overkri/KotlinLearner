package ledger.http

import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.install
import io.ktor.util.AttributeKey
import java.util.UUID

val RequestIdKey: AttributeKey<String> = AttributeKey("RequestId")

private val requestIdPlugin = createApplicationPlugin("RequestId") {
    onCall { call ->
        val requestId = call.request.headers["X-Request-Id"]
            ?.takeIf { it.isNotBlank() }
            ?: UUID.randomUUID().toString()
        call.attributes.put(RequestIdKey, requestId)
        call.response.headers.append("X-Request-Id", requestId)
    }
}

fun Application.installRequestId() {
    install(requestIdPlugin)
}
