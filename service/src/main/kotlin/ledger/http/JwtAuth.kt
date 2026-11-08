package ledger.http

import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.Route

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 11 · УПРАЖНЕНИЕ 11.1 — JWT-аутентификация
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Ktor Authentication, JWT verifier, protected routes
 *  Java-аналогия:  Spring Security + Keycloak adapter
 *  Тесты:          ./gradlew :service:test -Pmodule=11 --tests "ledger.m11.JwtAuthTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Настройте JWT-аутентификацию и защищённый маршрут.
 *
 *  ТРЕБОВАНИЯ
 *  1. configureJwt устанавливает Authentication с провайдером jwt("auth-jwt").
 *  2. verifier — переданный JWTVerifier (в тестах — локальный RSA-ключ).
 *  3. validate принимает токен только с непустым subject; иначе null -> 401.
 *  4. protected { ... } оборачивает маршруты в authenticate("auth-jwt").
 *  5. realm можно переопределить параметром.
 *
 *  ПОДСКАЗКИ
 *  - `install(Authentication) { jwt("auth-jwt") { verifier = verifier; validate { ... } } }`.
 *  - `authenticate("auth-jwt") { block() }`.
 *  - `JWTPrincipal(credential.payload)`.
 *
 *  ЛОВУШКА
 *  Почему проверка подписи без проверки issuer/audience недостаточна? Какой
 *  токен другого сервиса сможет пройти?
 * ═══════════════════════════════════════════════════════════
 */
fun Application.configureJwt(verifier: JWTVerifier, realm: String = "ledger"): Unit =
    TODO("m11.1.1")

fun Route.protected(block: Route.() -> Unit): Unit = TODO("m11.1.2")
