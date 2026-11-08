package ledger.migration

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 14 · УПРАЖНЕНИЕ 14.4 — Java → Kotlin migration
 * ═══════════════════════════════════════════════════════════
 *  Тема:           переписывание Java-кода на идиоматичный Kotlin
 *  Java-аналогия:  LegacyPricingEngine.java
 *  Тесты:          ./gradlew :migration-kata:test -Pmodule=14 --tests "ledger.m14.PricingEngineTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Перепишите LegacyPricingEngine на Kotlin так, чтобы результаты совпадали
 *  для всех входов.
 *
 *  ТРЕБОВАНИЯ
 *  1. quantity <= 0 -> IllegalArgumentException("quantity must be positive").
 *  2. STANDARD -> quantity * unitPrice.
 *  3. SILVER -> subtotal * 95 / 100 (целочисленное деление, как в Java).
 *  4. GOLD -> subtotal * 90 / 100.
 *  5. Неизвестный tier -> IllegalArgumentException("unknown tier: <tier>").
 *  6. Переполнение -> ArithmeticException (Math.multiplyExact).
 *  7. Никаких `!!`, `var` без необходимости, Java-измов.
 *
 *  ПОДСКАЗКИ
 *  - `when (tier) { ... }` — выражение.
 *  - `require(quantity > 0) { "quantity must be positive" }`.
 *  - `Math.multiplyExact(quantity.toLong(), unitPriceCents)`.
 *  - `else -> throw IllegalArgumentException("unknown tier: $tier")`.
 *
 *  ЛОВУШКА
 *  Чем `Int` отличается от `Long` при умножении в Kotlin и почему
 *  `quantity * unitPriceCents` может дать неверный тип/переполнение?
 * ═══════════════════════════════════════════════════════════
 */
class PricingEngine {
    fun priceCents(quantity: Int, unitPriceCents: Long, tier: String): Long = TODO("m14.4.1")
}
