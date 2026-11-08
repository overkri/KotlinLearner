# Модуль 05 — Функции, расширения, DSL

## Цель
Писать идиоматичный Kotlin-код поверх домена: extension-функции и операторы, scope-функции, типобезопасный DSL, делегаты свойств.

## Предусловия
Модуль 03–04. `Money`, `Transfer`, `TransferRequest`, `Account` готовы.

## Шпаргалка
- **Extension-функция** не меняет класс: `fun Money.format(): String`. Компилируется в статический метод с receiver первым параметром.
- **Оператор** — extension с `operator`: `plus`, `minus`, `times`, `compareTo`, `get`, `invoke`.
- **Scope-функции**:

| Функция | Receiver | Возвращает | Типичный случай |
|---|---|---|---|
| `let` | `it` | результат | nullable-цепочка, преобразование |
| `run` | `this` | результат | блок вычислений с доступом к членам |
| `with` | `this` | результат | несколько операций над объектом |
| `apply` | `this` | receiver | конфигурация/DSL |
| `also` | `it` | receiver | побочный эффект (лог, проверка) |

- **DSL**: receiver-лямбда + `@DslMarker`; builder-класс с `var`-полями и `build()`.
- **Делегаты**: `by lazy`, `Delegates.observable`, `Delegates.vetoable`, кастомный `ReadWriteProperty`.
- `vetoable` возвращает `false`, чтобы отклонить присваивание; `observable` просто реагирует после изменения.

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `MoneyUtils.add(a, b)` | `a + b` (extension `operator fun Money.plus`) |
| `Comparator.comparingLong(Money::getMinor)` | `compareTo` + `sorted()` |
| `if (x != null) { ... }` | `x?.let { ... }` |
| `builder.setX(...).setY(...).build()` | `dsl { x = ...; y = ... }` |
| `getter/setter` с проверкой диапазона | `var x by Delegates.vetoable(...)` |
| `static` utility class | top-level extension functions |
| `@FunctionalInterface` | `fun interface` |
| `this` в лямбде | receiver (`apply`, `with`, `run`) |

## Extension-функции и операторы
```kotlin
operator fun Money.plus(other: Money): Money {
    require(currency == other.currency) { "currency mismatch" }
    return Money(Math.addExact(minor, other.minor), currency)
}

val total = listOf(price, fee).total()
```
Важно: extensions не участвуют в полиморфизме. Если у класса есть метод с той же сигнатурой, победит метод класса. Extension не может получить доступ к `private`-полям.

## Scope-функции: правило выбора
- Нужен результат преобразования — `let`/`run`.
- Нужно сконфигурировать объект и вернуть его — `apply`.
- Нужен побочный эффект, но вернуть исходный объект — `also`.
- Несколько вызовов над одним объектом — `with`.

Не вкладывайте scope-функции глубже двух уровней: читаемость падает быстрее, чем экономится код.

## DSL и `@DslMarker`
```kotlin
@DslMarker
annotation class TransferDsl

@TransferDsl
class TransferRequestBuilder {
    var from: AccountId? = null
    fun build(): TransferRequest = TransferRequest(
        from = requireNotNull(from) { "from is required" },
        // ...
    )
}

fun transferRequest(block: TransferRequestBuilder.() -> Unit): TransferRequest =
    TransferRequestBuilder().apply(block).build()
```
`@DslMarker` запрещает неявно вызывать методы внешнего DSL из вложенного — иначе `from` мог бы «провалиться» не в тот builder.

## Пять ловушек
1. **Extension не переопределяет метод класса**. Если у класса есть `fun plus(...)`, extension с той же сигнатурой не будет вызван. Проверяйте сигнатуры.
2. **`apply` возвращает receiver, а не результат лямбды**. `val x = builder.apply { ... }` — это builder, а не `Unit`.
3. **`let` и `also` легко перепутать**. `let` возвращает результат лямбды, `also` — исходный объект; ошибка меняет тип и логику.
4. **`vetoable` молча игнорирует некорректное значение**. Если бизнесу нужна ошибка, используйте `require` в сеттере/методе, а не делегат.
5. **Operator overloading ради красоты**. `+` для «добавить в базу» или `*` для «конвертировать валюту» — плохая идея: операторы должны соответствовать математическому смыслу.
