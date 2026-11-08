# Модуль 02 — Null-safety и Java interop

## Цель
Перестать бояться `NullPointerException`: понимать, откуда берутся platform types, где компилятор действительно защищает, а где — нет, и как безопасно оборачивать legacy Java.

## Предусловия
Модули 00–01. Понадобятся `LegacyCustomerDirectory` и `LegacyCustomer` из `core/src/main/java/ledger/legacy`.

## Шпаргалка
- `String` — не null; `String?` — null допустим. Это проверяется компилятором, а не рантаймом.
- `?.` — вызов только если не null; `?:` — значение по умолчанию; `!!` — «упасть, если null».
- Smart cast: после `if (x != null)` компилятор считает `x` non-null в этой ветке, если `x` — `val` или локальная переменная.
- `isNullOrBlank()` / `isNullOrEmpty()` — extensions для `String?`.
- `takeIf { }` возвращает receiver или null; удобен для «нормализовать и проверить».
- `lateinit var` — для non-null свойства, которое инициализируется позже (не для примитивов).
- `by lazy { }` — потокобезопасная ленивая инициализация по умолчанию (`SYNCHRONIZED`).
- Java-методы без аннотаций дают **platform type** `T!`: Kotlin разрешает и `T`, и `T?`, но ответственность на вас.
- `@Nullable`/`@NonNull` (JSpecify, JetBrains annotations) превращают platform type в честный `T?`/`T`.

## Откуда берутся platform types
```java
public LegacyCustomer find(String id) { ... }   // может вернуть null
public String getName() { ... }                  // может вернуть null
```
```kotlin
val customer = legacy.find(id)      // LegacyCustomer!
println(customer.name.length)       // компилируется, но может упасть
```
Правильная граница — один адаптер, который превращает Java-типы в Kotlin-контракт:
```kotlin
private fun LegacyCustomer.toCustomer(): Customer = Customer(
    id = id,
    name = name,       // String! -> String?
    email = email,
)
```

## `lazy` и `lateinit`
| Инструмент | Когда использовать |
|---|---|
| `by lazy` | вычисление дорогое, результат не меняется, нужен потокобезопасный singleton |
| `lateinit var` | DI-контейнер/тест инициализирует позже; тип non-null, не примитив |
| `var x: T? = null` | значение действительно может отсутствовать |

`lazy` по умолчанию синхронизирован. Если инициализация гарантированно однопоточная, можно `LazyThreadSafetyMode.NONE`, но в серверном коде это редкое исключение.

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `Optional<String>` | `String?` |
| `x == null ? y : x` | `x ?: y` |
| `if (x != null) x.length()` | `x?.length` или smart cast |
| `Objects.requireNonNull(x)` | `requireNotNull(x)` |
| `x.orElseThrow(...)` | `x ?: error(...)` |
| `@Nullable String` | `String?` |
| `@NonNull String` | `String` |
| `String!` | platform type, проверяйте вручную |
| `synchronized { lazyInit() }` | `by lazy { }` |
| `volatile` + double-checked locking | `by lazy` / `AtomicReference` |

## Пять ловушек
1. **Platform type не делает код безопасным**. `legacy.find(id).name` компилируется, но падает, если `find` вернул null. Всегда проверяйте Java-границу.
2. **`!!` в эталонном коде запрещён**. Если вы «уверены», используйте `requireNotNull(x) { "why it must be present" }` — будет понятная ошибка и сообщение.
3. **`lateinit` не проверяется при компиляции**. Обращение до инициализации бросает `UninitializedPropertyAccessException`; для nullable-полей лучше `var x: T?`.
4. **`lazy` кэширует и исключение**. Если инициализатор бросил исключение, повторная попытка снова выполнит его; не путайте с «пустым» результатом.
5. **Java-коллекции — platform types**. `Map<String, String>` из Java может содержать null-значения; `map[key]` в Kotlin вернёт `String!`, а не `String?`.
