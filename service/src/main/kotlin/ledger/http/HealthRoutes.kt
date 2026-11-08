package ledger.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun interface HealthCheck {
    fun isReady(): Boolean
}

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 11 · УПРАЖНЕНИЕ 11.2 — Health endpoints
 * ═══════════════════════════════════════════════════════════
 *  Тема:           liveness vs readiness, dependency health
 *  Java-аналогия:  Spring Actuator /health/liveness, /health/readiness
 *  Тесты:          ./gradlew :service:test -Pmodule=11 --tests "ledger.m11.HealthRoutesTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Реализуйте два health-эндпоинта.
 *
 *  ТРЕБОВАНИЯ
 *  1. GET /health/live -> 200 HealthDto("UP") всегда (процесс жив).
 *  2. GET /health/ready -> 200 HealthDto("UP"), если health.isReady() == true.
 *  3. GET /health/ready -> 503 HealthDto("DOWN"), если false.
 *  4. Никакой аутентификации на health-маршрутах.
 *
 *  ПОДСКАЗКИ
 *  - `route("/health") { get("/live") { ... }; get("/ready") { ... } }`.
 *  - `call.respond(HttpStatusCode.ServiceUnavailable, HealthDto("DOWN"))`.
 *
 *  ЛОВУШКА
 *  Почему liveness не должен проверять БД? Что произойдёт с подом в Kubernetes,
 *  если БД временно недоступна, а liveness возвращает 503?
 * ═══════════════════════════════════════════════════════════
 */
fun Route.healthRoutes(health: HealthCheck): Unit = TODO("m11.2.1")
