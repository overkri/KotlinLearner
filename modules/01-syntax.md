# Модуль 01 — Синтаксис и выражения

## Цель
Писать простые Kotlin-функции без Java-измов: `val` по умолчанию, `when` и `if` как выражения, строковые шаблоны, циклы по диапазонам, `buildString`.

## Предусловия
Модуль 00 пройден: проект собирается, тесты запускаются по тегу.

## Шпаргалка
- `val` — read-only-ссылка, `var` — изменяемая. В эталонном коде `var` встречается редко.
- `if` и `when` возвращают значение, поэтому вместо тернарного оператора — `if`.
- `when` без аргумента — цепочка условий; с аргументом — аналог `switch`, но с проверкой исчерпываемости для `sealed`/`enum`.
- Диапазоны: `0..9` включает 9, `0 until 10` — нет, `10 downTo 0`, `step 2`.
- Строковые шаблоны: `"$name"`, `"${user.name}"`; литеральный `$` экранируется `\$`.
- `buildString { append(...) }` заменяет `StringBuilder` + `toString()`.
- Функция-выражение: `fun fee(x: Long): Long = x / 100` (без `return` и блока).
- `require(...)` — предусловие (IllegalArgumentException), `check(...)` — инвариант (IllegalStateException).
- `toLongOrNull()` — безопасный парсинг без исключения.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/basics/AmountFormat.kt` | `formatMinor(minor, currency)`: символы валют, JPY без центов, отрицательные суммы, неизвестная валюта | USD/EUR/GBP/JPY, ноль, отрицательные, неизвестный код |
| `core/src/main/kotlin/ledger/basics/AmountParser.kt` | `parseAmount(text): Long?` — ручной разбор десятичной строки | Корректные форматы, отрицательные, пробелы, `null` для мусора |
| `core/src/main/kotlin/ledger/basics/FeeSchedule.kt` | `feeBps(currency, amountMinor)`: `when` с диапазонами и `require` | Границы 999/1000/9999/10000, EUR 4999/5000, неизвестная валюта |
| `core/src/main/kotlin/ledger/basics/ReceiptSummary.kt` | `receiptSummary(amounts, currency)`: цикл, накопление итога, `buildString` | Нумерация, разделители, пустой список, отрицательные суммы |

## Рефакторинги
1. Замените `for (index in amounts.indices)` на `forEachIndexed` и сравните читаемость и аллокации.
2. Перепишите `parseAmount` через `BigDecimal` — что изменится в обработке переполнения и локали?
3. Уберите `require` из `feeBps`, оставив только положительные тесты. Какие баги станут возможны?

## Квиз
`quizzes/01-syntax.md` — 8 вопросов про `if/when`, диапазоны, строки, `val/var` и переполнение.

## Веха
CLI-сценарий: `receiptSummary(listOf(1234, 500), "USD")` печатает две строки и итог `$17.34`.

## Чеклист
- [ ] Все четыре упражнения решены.
- [ ] `./gradlew test -Pmodule=01` зелёный.
- [ ] Квиз пройден.
- [ ] В `PROGRESS.md` отмечены пункты 1.1–1.4.

## Gate
`./gradlew build -x test` — зелёный; `./gradlew test -Pmodule=01` на заглушках — только `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 01` — зелёный.
