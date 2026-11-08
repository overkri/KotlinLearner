package ledger.nullsafety

import ledger.legacy.LegacyCustomerDirectory

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 2 · УПРАЖНЕНИЕ 2.1 — Безопасная обёртка справочника
 * ═══════════════════════════════════════════════════════════
 *  Тема:           platform types, ?., ?:, smart cast
 *  Java-аналогия:  Optional.ofNullable + проверки на null
 *  Тесты:          ./gradlew :core:test -Pmodule=02 --tests "ledger.m02.CustomerDirectoryTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Оберните Java-справочник так, чтобы снаружи не было ни одного platform type
 *  и ни одного неожиданного NullPointerException.
 *
 *  ТРЕБОВАНИЯ
 *  1. find(id) возвращает Customer? и null, если клиента нет.
 *  2. displayName(id): имя клиента; если имя null или состоит из пробелов —
 *     вернуть id; если клиента нет — "Unknown customer".
 *  3. emailDomain(id): часть email после '@'; null, если клиента/email нет или
 *     в email нет '@'. Пустая строка не должна считаться доменом.
 *  4. Не использовать !!.
 *
 *  ПОДСКАЗКИ
 *  - Вспомогательная функция LegacyCustomer.toCustomer() уже есть в Customer.kt.
 *  - `legacy.find(id) ?: return null` — ранний выход.
 *  - Для email удобно `substringAfter('@', missingDelimiterValue = "")`.
 *
 *  ЛОВУШКА
 *  Почему `val customer = legacy.find(id); customer.name` может упасть, хотя
 *  компилятор Kotlin не подсвечивает ошибку?
 * ═══════════════════════════════════════════════════════════
 */
class CustomerDirectory(
    private val legacy: LegacyCustomerDirectory = LegacyCustomerDirectory(),
) {
    fun find(id: String): Customer? = TODO("m02.1.1")

    fun displayName(id: String): String = TODO("m02.1.2")

    fun emailDomain(id: String): String? = TODO("m02.1.3")
}
