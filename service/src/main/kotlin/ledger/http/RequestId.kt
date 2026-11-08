package ledger.http

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey

val RequestIdKey: AttributeKey<String> = AttributeKey("RequestId")

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 11 · УПРАЖНЕНИЕ 11.4 — Request ID
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Ktor plugin, ApplicationCall attributes, headers
 *  Java-аналогия:  OncePerRequestFilter + MDC
 *  Тесты:          ./gradlew :service:test -Pmodule=11 --tests "ledger.m11.RequestIdTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Добавьте каждому запросу X-Request-Id: из заголовка или сгенерированный.
 *
 *  ТРЕБОВАНИЯ
 *  1. Если клиент передал X-Request-Id — использовать его.
 *  2. Иначе сгенерировать UUID.
 *  3. Положить значение в call.attributes[RequestIdKey].
 *  4. Вернуть X-Request-Id в response headers.
 *  5. Плагин не должен ломать запрос при отсутствии заголовка.
 *
 *  ПОДСКАЗКИ
 *  - `createApplicationPlugin("RequestId") { onCall { call -> ... } }`.
 *  - `call.request.headers["X-Request-Id"]`.
 *  - `call.response.headers.append("X-Request-Id", id)`.
 *
 *  ЛОВУШКА
 *  Почему request id полезен в логах и трассировке? Чем он отличается от
 *  trace id и correlation id?
 * ═══════════════════════════════════════════════════════════
 */
fun Application.installRequestId(): Unit = TODO("m11.4.1")
