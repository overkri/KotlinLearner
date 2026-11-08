package ledger.domain

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 3 · УПРАЖНЕНИЕ 3.1 — Money
 * ═══════════════════════════════════════════════════════════
 *  Тема:           data class, init-валидация, companion object
 *  Java-аналогия:  record + статические фабрики + equals/hashCode
 *  Тесты:          ./gradlew :core:test -Pmodule=03 --tests "ledger.m03.MoneyTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Реализуйте денежную сумму в минорных единицах. Сумма всегда неотрицательна;
 *  направление операции задаётся отдельно (DEBIT/CREDIT).
 *
 *  ТРЕБОВАНИЯ
 *  1. init: minor >= 0, иначе IllegalArgumentException с сообщением
 *     "minor must be non-negative".
 *  2. isZero == (minor == 0).
 *  3. companion object: zero(currency) и ofMajor(major, currency).
 *  4. ofMajor учитывает Currency.fractionDigits: USD 12 -> 1200, JPY 12 -> 12.
 *  5. ofMajor с отрицательным major — IllegalArgumentException
 *     "major must be non-negative".
 *  6. Money(1200, USD) == Money(1200, USD); currency входит в equals.
 *
 *  ПОДСКАЗКИ
 *  - `data class` сам генерирует equals/hashCode/toString/copy/componentN.
 *  - `init { require(...) { "..." } }` выполняется при каждом konstruktore.
 *  - `when (currency.fractionDigits) { 0 -> 1L; 2 -> 100L; else -> error(...) }`.
 *
 *  ЛОВУШКА
 *  Чем `data class` отличается от Java `record` в наследовании и копировании?
 *  Что произойдёт, если добавить `var minor` вместо `val`?
 * ═══════════════════════════════════════════════════════════
 */
data class Money(
    val minor: Long,
    val currency: Currency,
) {
    init {
        TODO("m03.1.1")
    }

    val isZero: Boolean
        get() = TODO("m03.1.2")

    companion object {
        fun zero(currency: Currency): Money = TODO("m03.1.3")

        fun ofMajor(major: Long, currency: Currency): Money = TODO("m03.1.4")
    }
}
