package ledger.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun interface HealthCheck {
    fun isReady(): Boolean
}

fun Route.healthRoutes(health: HealthCheck) {
    get("/health/live") {
        call.respond(HealthDto("UP"))
    }
    get("/health/ready") {
        if (health.isReady()) {
            call.respond(HealthDto("UP"))
        } else {
            call.respond(HttpStatusCode.ServiceUnavailable, HealthDto("DOWN"))
        }
    }
}
