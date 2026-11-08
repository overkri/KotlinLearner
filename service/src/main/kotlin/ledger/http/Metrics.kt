package ledger.http

import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 11 · УПРАЖНЕНИЕ 11.3 — Метрики Prometheus
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Micrometer, Prometheus registry, Ktor metrics plugin
 *  Java-аналогия:  Actuator /prometheus + Micrometer
 *  Тесты:          ./gradlew :service:test -Pmodule=11 --tests "ledger.m11.MetricsTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Подключите Micrometer и отдайте метрики в формате Prometheus.
 *
 *  ТРЕБОВАНИЯ
 *  1. configureMetrics создаёт PrometheusMeterRegistry(PrometheusConfig.DEFAULT).
 *  2. Устанавливает MicrometerMetrics с этим registry.
 *  3. Биндит JVM-метрики: ClassLoaderMetrics, JvmMemoryMetrics, JvmGcMetrics,
 *     JvmThreadMetrics (bindTo(registry)).
 *  4. metricsRoute отдаёт GET /metrics с registry.scrape() и Content-Type text/plain.
 *  5. Возвращает registry, чтобы тест мог проверить scrape.
 *
 *  ПОДСКАЗКИ
 *  - `install(MicrometerMetrics) { registry = meterRegistry }`.
 *  - `ClassLoaderMetrics().bindTo(registry)`.
 *  - `call.respondText(registry.scrape(), ContentType.Text.Plain)`.
 *
 *  ЛОВУШКА
 *  Почему метрики с высокой кардинальностью (userId, accountId в labels)
 *  могут убить Prometheus? Какой label допустим, а какой — нет?
 * ═══════════════════════════════════════════════════════════
 */
fun Application.configureMetrics(): PrometheusMeterRegistry = TODO("m11.3.1")

fun Route.metricsRoute(registry: PrometheusMeterRegistry): Unit = TODO("m11.3.2")
