package ledger.functional

import ledger.domain.Money

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 5 · УПРАЖНЕНИЕ 5.1 — Операторы Money
 * ═══════════════════════════════════════════════════════════
 *  Тема:           extension functions, operator overloading, compareTo
 *  Java-аналогия:  статические утилиты Money.add / Money.subtract
 *  Тесты:          ./gradlew :core:test -Pmodule=05 --tests "ledger.m05.MoneyOperatorsTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Добавьте Money арифметику через extension-операторы. Сам класс Money
 *  менять нельзя — это хорошая практика: расширение не трогает домен.
 *
 *  ТРЕБОВАНИЯ
 *  1. Money + Money: та же валюта, иначе IllegalArgumentException "currency mismatch".
 *  2. Money - Money: та же валюта; результат не может быть отрицательным,
 *     иначе IllegalArgumentException "result must be non-negative".
 *  3. Money * Int: factor >= 0, иначе IllegalArgumentException
 *     "factor must be non-negative"; умножение с проверкой переполнения.
 *  4. Money.compareTo(Money): та же валюта, иначе "currency mismatch";
 *     сравнение по minor.
 *  5. Iterable<Money>.total(): пустая коллекция -> IllegalArgumentException
 *     "cannot total an empty collection"; разные валюты -> "currency mismatch".
 *
 *  ПОДСКАЗКИ
 *  - `operator fun Money.plus(other: Money): Money`.
 *  - `compareTo` достаточно объявить extension — `<`, `<=`, `>` заработают.
 *  - `Math.addExact` / `Math.multiplyExact` предотвращают тихое переполнение.
 *  - `fold` с начальным значением удобен для total().
 *
 *  ЛОВУШКА
 *  Почему `Money + Money` с разными валютами — ошибка, а не конвертация?
 *  Где должна жить логика курсов валют?
 * ═══════════════════════════════════════════════════════════
 */
operator fun Money.plus(other: Money): Money = TODO("m05.1.1")

operator fun Money.minus(other: Money): Money = TODO("m05.1.2")

operator fun Money.times(factor: Int): Money = TODO("m05.1.3")

operator fun Money.compareTo(other: Money): Int = TODO("m05.1.4")

fun Iterable<Money>.total(): Money = TODO("m05.1.5")
