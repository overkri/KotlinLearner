package ledger.basics

import java.time.Instant

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 0 · УПРАЖНЕНИЕ 0.2 — Первый тест с MockK
 * ═══════════════════════════════════════════════════════════
 *  Тема:           constructor injection, fun interface, MockK
 *  Java-аналогия:  @Component с зависимостью Clock и Mockito-мок
 *  Тесты:          ./gradlew :core:test -Pmodule=00 --tests "ledger.m00.ReceiptPrinterTest"
 *  Уровень:        ★☆☆
 *
 *  ЗАДАНИЕ
 *  Реализуйте печать квитанции, используя внедрённые часы. В тестах часы
 *  подменяются MockK-моком, поэтому реальное время в результат не попадает.
 *
 *  ТРЕБОВАНИЯ
 *  1. Формат строки: "<amountMinor> <currency> @ <ISO-8601 Instant>".
 *     Например: "1234 USD @ 2026-11-08T12:00:00Z".
 *  2. clock.now() вызывается ровно один раз за вызов print.
 *  3. Отрицательная сумма должна приводить к IllegalArgumentException
 *     с сообщением "amount must be positive".
 *
 *  ПОДСКАЗКИ
 *  - Instant.toString() уже даёт ISO-8601 в UTC.
 *  - require(...) — аналог Objects.requireNonNull/IllegalArgumentException.
 *
 *  ЛОВУШКА
 *  Чем инъекция Clock через конструктор лучше вызова Instant.now() внутри метода?
 * ═══════════════════════════════════════════════════════════
 */
fun interface Clock {
    fun now(): Instant
}

class ReceiptPrinter(private val clock: Clock) {
    fun print(amountMinor: Long, currency: String): String = TODO("m00.2.1")
}
