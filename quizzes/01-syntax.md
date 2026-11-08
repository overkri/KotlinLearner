# Квиз 01 — Синтаксис и выражения

## 1. Что выведет?
```kotlin
val x = if (1 > 2) "a" else "b"
println(x)
```
<details><summary>Ответ</summary>`b`. `if` — выражение и возвращает значение.</details>

## 2. Сколько раз выполнится цикл?
```kotlin
for (i in 0..3) print(i)
for (i in 0 until 3) print(i)
```
<details><summary>Ответ</summary>`0123` и `012`. `..` включает 3, `until` — нет.</details>

## 3. Скомпилируется ли?
```kotlin
fun fee(amount: Long): Long = amount / 100
```
```java
long fee(long amount) { return amount / 100; }
```
<details><summary>Ответ</summary>Да. Kotlin-функция с expression body компилируется в обычный метод. Возвращаемое значение — результат выражения; `return` не нужен.</details>

## 4. Что вернёт `"12.345".toLongOrNull()`?
<details><summary>Ответ</summary>`null`: строка не является целым числом. Метод не бросает исключение, в отличие от `toLong()`.</details>

## 5. Почему этот код не скомпилируется?
```kotlin
val price = "$12.34"
```
<details><summary>Ответ</summary>`$` начинает строковый шаблон, а `12` — не идентификатор. Нужно `"\$12.34"` или `${'$'}12.34`.</details>

## 6. Что вернёт `when` без `else` для `enum`?
```kotlin
enum class Status { ACTIVE, FROZEN }
fun label(s: Status) = when (s) {
    Status.ACTIVE -> "ok"
}
```
<details><summary>Ответ</summary>Ошибка компиляции: `when` должен быть исчерпывающим. Добавьте `Status.FROZEN -> ...` или `else`. Компилятор не даст забыть новое значение enum.</details>

## 7. Чем `require` отличается от `check`?
<details><summary>Ответ</summary>`require` проверяет аргументы/предусловия и бросает `IllegalArgumentException`; `check` проверяет состояние объекта/инвариант и бросает `IllegalStateException`.</details>

## 8. Что не так с этим накоплением?
```kotlin
var text = ""
for (i in 1..1000) text += "$i,"
```
<details><summary>Ответ</summary>Строки неизменяемы, каждая `+=` создаёт новую строку — O(n²) по памяти. Используйте `buildString { }` или `StringBuilder`.</details>
