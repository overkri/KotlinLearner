package ledger.testing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.Date

/**
 * Локальные RSA-ключи и подпись токенов для тестов модуля 11.
 * Не требует запуска Keycloak или JWKS-сервера.
 */
object TestJwt {

    const val ISSUER: String = "https://test-issuer"
    const val AUDIENCE: String = "ledger-api"

    val keyPair: KeyPair = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }.generateKeyPair()

    fun verifier(
        issuer: String = ISSUER,
        audience: String = AUDIENCE,
    ): JWTVerifier = JWT.require(Algorithm.RSA256(keyPair.public as RSAPublicKey, null))
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun token(
        subject: String = "user-1",
        issuer: String = ISSUER,
        audience: String = AUDIENCE,
        expiresAt: Instant = Instant.now().plusSeconds(3_600),
    ): String = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject(subject)
        .withIssuedAt(Date.from(Instant.now()))
        .withExpiresAt(Date.from(expiresAt))
        .withClaim("scope", "transfers:write accounts:read")
        .sign(Algorithm.RSA256(keyPair.public as RSAPublicKey, keyPair.private as RSAPrivateKey))
}
