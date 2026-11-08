package ledger.http

import java.util.concurrent.atomic.AtomicBoolean

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 11 · УПРАЖНЕНИЕ 11.5 — Graceful shutdown
 * ═══════════════════════════════════════════════════════════
 *  Тема:           readiness, атомарное состояние, drain
 *  Java-аналогия:  Spring Boot graceful shutdown + Actuator readiness
 *  Тесты:          ./gradlew :service:test -Pmodule=11 --tests "ledger.m11.ShutdownCoordinatorTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Реализуйте координатор остановки: при сигнале shutdown сервис перестаёт
 *  считаться готовым, но ещё может дренировать запросы.
 *
 *  ТРЕБОВАНИЯ
 *  1. initially isReady() == true.
 *  2. beginShutdown() переводит в состояние «не готов».
 *  3. Повторный beginShutdown() идемпотентен.
 *  4. isReady() потокобезопасен (AtomicBoolean или volatile).
 *  5. После остановки вызовы isReady() всегда false.
 *
 *  ПОДСКАЗКИ
 *  - `AtomicBoolean(false)` + `compareAndSet`.
 *  - В Ktor подписка: `monitor.subscribe(ApplicationStopping) { coordinator.beginShutdown() }`.
 *  - Readiness-роут должен вернуть 503, пока под не будет удалён из балансировщика.
 *
 *  ЛОВУШКА
 *  Почему недостаточно просто `System.exit(0)`? Что случится с in-flight
 *  запросами и сообщениями из Kafka?
 * ═══════════════════════════════════════════════════════════
 */
class ShutdownCoordinator {

    private val stopping = AtomicBoolean(false)

    fun beginShutdown(): Unit = TODO("m11.5.1")

    fun isReady(): Boolean = TODO("m11.5.2")
}
