# Квиз 04 — Коллекции и Sequence

## 1. Что выведет?
```kotlin
println(listOf(1, 2, 3, 4).filter { it % 2 == 0 }.map { it * 10 })
```
<details><summary>Ответ</summary>`[20, 40]`. `filter` оставляет чётные, `map` умножает на 10.</details>

## 2. Когда выполнится этот конвейер?
```kotlin
val seq = listOf(1, 2, 3).asSequence().onEach { println("visit $it") }
println("before")
println(seq.toList())
```
<details><summary>Ответ</summary>`before` напечатается первым, затем `visit 1`, `visit 2`, `visit 3`, `[1, 2, 3]`. Sequence ленив до терминальной операции.</details>

## 3. Что вернёт `associateBy` при дубликатах?
```kotlin
listOf("a" to 1, "a" to 2).associateBy({ it.first }, { it.second })
```
<details><summary>Ответ</summary>`{a=2}`. Побеждает последнее значение.</details>

## 4. Чем `groupBy` отличается от `groupingBy`?
<details><summary>Ответ</summary>`groupBy` сразу создаёт `Map<K, List<V>>`. `groupingBy` возвращает ленивый `Grouping`, к которому применяют `eachCount`, `fold`, `reduce` — без материализации всех групп.</details>

## 5. Что не так с этим кодом?
```kotlin
val rows = entries.asSequence()
    .filter { it.amount.minor > 100 }
rows.forEach { println(it) }
rows.forEach { println(it) }
```
<details><summary>Ответ</summary>Sequence может быть одноразовым; повторный проход не гарантирован. Кроме того, каждый `forEach` заново вычисляет конвейер. Материализуйте `toList()`, если нужен повторный обход.</details>

## 6. Как отсортировать по убыванию количества, затем по id?
<details><summary>Ответ</summary>`sortedWith(compareByDescending<Pair<AccountId, Int>> { it.second }.thenBy { it.first.value })`.</details>

## 7. Что делает `partition`?
<details><summary>Ответ</summary>Делит коллекцию на две по предикату и возвращает `Pair(first, second)`, где `first` — подходящие, `second` — остальные; порядок сохраняется. Удобно деструктурировать.</details>

## 8. `List` в Kotlin — это immutable-коллекция?
<details><summary>Ответ</summary>Нет. Это read-only интерфейс. Объект может быть `MutableList`, переданным как `List`; другая ссылка может его изменить. Для настоящей неизменяемости нужна дисциплина или неизменяемая библиотека.</details>
