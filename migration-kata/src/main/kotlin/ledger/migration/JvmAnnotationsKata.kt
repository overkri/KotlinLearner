package ledger.migration

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 14 · УПРАЖНЕНИЕ 14.6 — Interop-аннотации
 * ═══════════════════════════════════════════════════════════
 *  Тема:           @JvmOverloads, @Throws, вызов Kotlin из Java
 *  Java-аналогия:  перегрузки и checked exceptions
 *  Тесты:          ./gradlew :migration-kata:test -Pmodule=14 --tests "ledger.m14.JvmAnnotationsTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Сделайте Kotlin-API удобным для Java-вызывающих.
 *
 *  ТРЕБОВАНИЯ
 *  1. price(quantity, unitPriceCents, tier = "STANDARD") с @JvmOverloads:
 *     Java должна видеть и price(int, long), и price(int, long, String).
 *  2. price делегирует PricingEngine.
 *  3. parseTier(value): trim + uppercase; допустимы STANDARD/SILVER/GOLD;
 *     иначе IllegalArgumentException("unknown tier: <original>").
 *  4. parseTier помечен @Throws(IllegalArgumentException::class), чтобы
 *     Java-документация/IDE видели исключение.
 *
 *  ПОДСКАЗКИ
 *  - `@JvmOverloads` генерирует Java-перегрузки для default-аргументов.
 *  - `@Throws` нужен для Java-вызывающих; Kotlin checked exceptions не требует.
 *  - `value.trim().uppercase()` не меняет исходное сообщение об ошибке.
 *
 *  ЛОВУШКА
 *  Почему без @JvmOverloads Java-код не увидит `price(2, 100)`? Что именно
 *  генерирует Kotlin для default-аргументов?
 * ═══════════════════════════════════════════════════════════
 */
class JvmAnnotationsKata {

    @JvmOverloads
    fun price(quantity: Int, unitPriceCents: Long, tier: String = "STANDARD"): Long =
        TODO("m14.6.1")

    @Throws(IllegalArgumentException::class)
    fun parseTier(value: String): String = TODO("m14.6.2")
}
