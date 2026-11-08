package ledger.basics

import ledger.legacy.LegacyFeeCalculator

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 0 · УПРАЖНЕНИЕ 0.1 — Мост в Java
 * ═══════════════════════════════════════════════════════════
 *  Тема:           вызов Java из Kotlin, platform types, аргументы по умолчанию
 *  Java-аналогия:  обёртка над legacy-сервисом + перегрузки вместо default-аргументов
 *  Тесты:          ./gradlew :core:test -Pmodule=00 --tests "ledger.m00.FeeCalculatorBridgeTest"
 *  Уровень:        ★☆☆
 *
 *  ЗАДАНИЕ
 *  Оберните [LegacyFeeCalculator] так, чтобы Kotlin-код мог вызывать его без
 *  перегрузок и без риска получить null.
 *
 *  ТРЕБОВАНИЯ
 *  1. feeFor(amountMinor, currency = "USD") считает комиссию через Java-класс.
 *  2. feeForUsd(amountMinor) вызывает именно Java-перегрузку с одним аргументом.
 *  3. describe(currency) никогда не возвращает null: для неизвестной валюты —
 *     строка "Unknown currency".
 *  4. Класс принимает LegacyFeeCalculator в конструкторе, значение по умолчанию —
 *     новый LegacyFeeCalculator().
 *
 *  ПОДСКАЗКИ
 *  - Java-метод, возвращающий String, виден в Kotlin как String! (platform type).
 *  - Для null-safety используйте ?: — это не то же самое, что !!.
 *
 *  ЛОВУШКА
 *  Почему сигнатура `fun describe(...): String` без ?: потенциально опасна,
 *  хотя компилятор Kotlin не требует проверки на null?
 * ═══════════════════════════════════════════════════════════
 */
class FeeCalculatorBridge(
    private val legacy: LegacyFeeCalculator = LegacyFeeCalculator(),
) {
    fun feeFor(amountMinor: Long, currency: String = "USD"): Long = TODO("m00.1.1")

    fun feeForUsd(amountMinor: Long): Long = TODO("m00.1.2")

    fun describe(currency: String): String = TODO("m00.1.3")
}
