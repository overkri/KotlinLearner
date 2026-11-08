package ledger.functional

import ledger.domain.Money
import kotlin.properties.Delegates

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 5 · УПРАЖНЕНИЕ 5.4 — Делегаты свойств
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Delegates.vetoable, свойство с валидацией
 *  Java-аналогия:  setter с проверкой диапазона
 *  Тесты:          ./gradlew :core:test -Pmodule=05 --tests "ledger.m05.TransferLimitsTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Ограничьте максимальную сумму перевода диапазоном 1..1_000_000 минорных
 *  единиц. Некорректное присваивание должно игнорироваться, а не бросать
 *  исключение — это vetoable-делегат.
 *
 *  ТРЕБОВАНИЯ
 *  1. maxMinor инициализируется initialMaxMinor.
 *  2. Присваивание значения вне 1..1_000_000 не меняет свойство.
 *  3. Присваивание валидного значения меняет свойство.
 *  4. allows(amount): true, если amount.minor <= maxMinor.
 *
 *  ПОДСКАЗКИ
 *  - `var maxMinor: Long by Delegates.vetoable(initialMaxMinor) { _, _, new -> ... }`.
 *  - Лямбда возвращает Boolean: true — принять, false — отклонить.
 *  - `allows` — обычная функция, не extension.
 *
 *  ЛОВУШКА
 *  Чем `vetoable` отличается от `observable` и почему vetoable не бросает
 *  исключение? Где вы предпочтёте require(...)?
 * ═══════════════════════════════════════════════════════════
 */
class TransferLimits(initialMaxMinor: Long) {
    var maxMinor: Long by Delegates.vetoable(initialMaxMinor) { _, _, _ ->
        TODO("m05.4.1")
    }

    fun allows(amount: Money): Boolean = TODO("m05.4.2")
}
