# 01 — Синтаксис и выражения

## Главное отличие от Java
В Kotlin почти всё — выражение. `if`, `when`, `try`, блок `run { }` возвращают значение, поэтому не нужны тернарный оператор и временные переменные «на всякий случай». Код становится короче, но злоупотребление вложенными выражениями ухудшает читаемость.

```kotlin
val label = if (amountMinor < 0) "debit" else "credit"
val band = when {
    amountMinor < 1_000 -> "small"
    amountMinor < 10_000 -> "medium"
    else -> "large"
}
```

## `val` и `var`
- `val` — ссылка не переназначается. Объект при этом может быть изменяемым (`MutableList`).
- `var` нужен для накопления в цикле и для свойств с сеттером. В бизнес-логике старайтесь обходиться без него.
- В Java `final` часто опускают; в Kotlin `val` — норма по умолчанию, `var` — осознанное исключение.

## `when` без `switch`-провалов
```kotlin
val fee = when (currency) {
    "USD", "EUR" -> 30
    "JPY" -> 0
    else -> 50
}
```
`when` по `enum` или `sealed` без `else` проверяется на исчерпываемость компилятором. Это главный инструмент вместо цепочек `instanceof` и `switch`.

## Строки
```kotlin
val text = "amount=$amountMinor ${currency.lowercase()}"
val dollar = "\$12.34"           // экранирование
val raw = """C:\temp\file"""     // raw string, без экранирования
```
Для сборки больших строк используйте `buildString`, а не `+` в цикле: конкатенация создаёт промежуточные объекты.

## Циклы и диапазоны
| Kotlin | Смысл |
|---|---|
| `0..9` | 0, 1, …, 9 |
| `0 until 10` | 0, 1, …, 9 |
| `10 downTo 0` | 10, 9, …, 0 |
| `0..10 step 2` | 0, 2, 4, 6, 8, 10 |
| `list.indices` | `0 until list.size` |
| `list.lastIndex` | `size - 1`, или `-1` для пустого списка |

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `final long x = 1;` | `val x = 1L` |
| `long y = 1; y = 2;` | `var y = 1L; y = 2` |
| `x > 0 ? a : b` | `if (x > 0) a else b` |
| `switch (s) { case "A": ... }` | `when (s) { "A" -> ... }` |
| `for (int i = 0; i < n; i++)` | `for (i in 0 until n)` |
| `for (int i = n; i >= 0; i--)` | `for (i in n downTo 0)` |
| `String.format("%.2f", x)` | ручное форматирование / `"%.2f".format(x)` |
| `new StringBuilder()` | `buildString { }` |
| `Long.parseLong(s)` | `s.toLong()` (бросит), `s.toLongOrNull()` (вернёт null) |
| `Objects.requireNonNull` / `if (...) throw` | `require(...)`, `check(...)` |

## Пять ловушек
1. **`..` включает верхнюю границу**. `for (i in 0..list.size)` — классический `IndexOutOfBoundsException`; используйте `until` или `indices`.
2. **`$` в строке — начало шаблона**. `"$12.34"` не скомпилируется; пишите `"\$12.34"` или `${'$'}12.34`.
3. **`when` без `else` по `String` не исчерпывающий**. Компилятор потребует `else`; для `enum`/`sealed` — не потребует, и это преимущество.
4. **Целочисленное деление**. `1234 / 100` равно `12`, а не `12.34`. В финансах храните минорные единицы в `Long` и форматируйте только на границе.
5. **`return` внутри лямбды**. В `forEach { return }` в Kotlin возможен non-local return из inline-функции; в Java это было бы `continue`. Для циклов с ранним выходом используйте `for` или `firstOrNull`.
