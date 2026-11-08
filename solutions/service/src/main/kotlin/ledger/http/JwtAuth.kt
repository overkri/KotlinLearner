package ledger.http

import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.Route

fun Application.configureJwt(verifier: JWTVerifier, realm: String = "ledger") {
    install(Authentication) {
        jwt("auth-jwt") {
            this.realm = realm
            this.verifier(verifier)
            validate { credential ->
                if (credential.payload.subject.isNullOrBlank()) {
                    null
                } else {
                    JWTPrincipal(credential.payload)
                }
            }
        }
    }
}

fun Route.protected(block: Route.() -> Unit) {
    authenticate("auth-jwt") {
        block()
    }
}
