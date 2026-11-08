# Квиз 00 — Старт и инструменты

## 1. Скомпилируется ли код?
```kotlin
var balance = 100L
balance = 200L
val currency = "USD"
currency = "EUR"
```
<details>
<summary>Ответ</summary>
Нет. `balance` можно менять, `currency` — нельзя: `val` означает read-only-ссылку, а не «immutable объект». Ошибка компиляции на строке `currency = "EUR"`.
</details>

## 2. Что выведет код?
```kotlin
fun describe(x: String?): String = x ?: "unknown"
println(describe(null))
println(describe("USD"))
```
<details>
<summary>Ответ</summary>
`unknown` и `USD`. `?:` — элвис-оператор: возвращает левую часть, если она не null, иначе правую.
</details>

## 3. Почему этот код может упасть в рантайме?
```java
public static String describe(String currency) { return null; }
```
```kotlin
val name: String = LegacyFeeCalculator.describe("XYZ")
println(name.length)
```
<details>
<summary>Ответ</summary>
Java-метод возвращает platform type `String!`. Kotlin не заставляет проверять null, поэтому `name.length` бросит `NullPointerException`. Нужно `val name: String? = ...` или `?:`.
</details>

## 4. Что делает `TODO("m00.1.1")`?
<details>
<summary>Ответ</summary>
Бросает `kotlin.NotImplementedError` с указанным сообщением. Это не «пустая заглушка», а явный сигнал, что упражнение не решено. В тестах это ожидаемое падение.
</details>

## 5. Какой тест запустит команда `./gradlew test -Pmodule=00`?
```kotlin
@Tag("m00")
class A { @Test fun `a`() {} }

@Tag("m01")
class B { @Test fun `b`() {} }
```
<details>
<summary>Ответ</summary>
Только `A`. Корневой build-скрипт при `-Pmodule=00` добавляет `useJUnitPlatform { includeTags("m00") }`. Класс `B` будет отфильтрован.
</details>

## 6. Создаёт ли Kotlin Java-перегрузку для default-аргумента?
```kotlin
fun fee(amount: Long, currency: String = "USD") = ...
```
<details>
<summary>Ответ</summary>
Нет. Для Java существует только `fee(long, String)` и synthetic `fee$default(...)`. Чтобы Java увидела `fee(long)`, нужна аннотация `@JvmOverloads` или явная перегрузка.
</details>

## 7. Что проверяет `verify(exactly = 1) { clock.now() }`?
<details>
<summary>Ответ</summary>
Что метод `now()` был вызван ровно один раз за время теста. Если `ReceiptPrinter` вызывает часы дважды (например, для форматирования и для аудита), тест упадёт.
</details>

## 8. Зачем нужен `jvmToolchain(21)`, если Gradle уже запущен на JDK 25?
<details>
<summary>Ответ</summary>
Toolchain задаёт JDK для компиляции, тестов и запуска, независимо от JVM самого Gradle. Это делает сборку воспроизводимой на машинах с разными JDK и гарантирует, что проект не начнёт случайно использовать API из JDK 25.
</details>
