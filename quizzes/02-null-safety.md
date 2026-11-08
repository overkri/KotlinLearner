# Квиз 02 — Null-safety и Java interop

## 1. Скомпилируется ли?
```kotlin
val s: String = null
```
<details><summary>Ответ</summary>Нет. `String` non-null; нужен `String?`.</details>

## 2. Что выведет?
```kotlin
val s: String? = null
println(s?.length)
println(s?.length ?: -1)
```
<details><summary>Ответ</summary>`null` и `-1`. `?.` возвращает null, `?:` подставляет fallback.</details>

## 3. Почему этот код компилируется, но может упасть?
```java
public String email() { return null; }
```
```kotlin
val email = legacy.email()
println(email.length)
```
<details><summary>Ответ</summary>`legacy.email()` имеет platform type `String!`. Kotlin разрешает обращение как к non-null, но в рантайме будет NPE. Нужно `val email: String? = ...` или `?:`.</details>

## 4. Чем `requireNotNull` лучше `!!`?
<details><summary>Ответ</summary>`requireNotNull(x) { "message" }` бросает `IllegalArgumentException` с осмысленным сообщением. `!!` бросает `NullPointerException` без контекста. В бизнес-коде важно, что именно и почему отсутствовало.</details>

## 5. Что делает smart cast в этом коде?
```kotlin
fun lengthOrZero(s: String?): Int {
    if (s != null) return s.length
    return 0
}
```
<details><summary>Ответ</summary>Внутри `if (s != null)` компилятор считает `s` типом `String`, поэтому `s.length` доступен без `?.`. Для `var`, который может измениться из другого потока/замыкания, smart cast может быть запрещён.</details>

## 6. Когда `lateinit` лучше `by lazy`?
<details><summary>Ответ</summary>`lateinit` — когда значение внедряется извне после создания объекта (например, `@BeforeEach` в тесте) и тип non-null. `by lazy` — когда значение вычисляется самим объектом при первом обращении и дальше не меняется.</details>

## 7. Что не так с `by lazy` и исключением?
<details><summary>Ответ</summary>Если инициализатор бросил исключение, lazy-значение не закэшируется; следующее обращение попробует снова. Это может повторять дорогую/падающую операцию. Для «запомнить ошибку» нужен собственный `Result`/sealed-тип.</details>

## 8. Как Java-аннотации влияют на Kotlin?
<details><summary>Ответ</summary>`@Nullable` превращает `String!` в `String?`, `@NonNull` — в `String`. Без аннотаций Kotlin видит platform type и не заставляет проверять null. Поэтому legacy-код без аннотаций оборачивают в адаптер.</details>
