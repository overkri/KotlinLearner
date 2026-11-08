# Квиз 05 — Функции, расширения, DSL

## 1. Что выведет?
```kotlin
val s: String? = null
println(s?.let { "len=${it.length}" })
println(s?.also { println("side") })
```
<details><summary>Ответ</summary>`null` и `null`. Ни `let`, ни `also` не выполняются для null; `also` вернул бы исходную строку, но её нет.</details>

## 2. Чем `apply` отличается от `also`?
<details><summary>Ответ</summary>`apply` получает receiver `this` и возвращает receiver; `also` получает `it` и тоже возвращает receiver. Разница в доступе к членам (this vs it) и стиле; `apply` чаще для конфигурации, `also` — для побочных эффектов.</details>

## 3. Что вернёт `builder.apply { from = a }`?
<details><summary>Ответ</summary>Сам `builder`, а не результат последнего выражения. Если нужен результат `build()`, пишите `builder.apply { ... }.build()`.</details>

## 4. Зачем нужен `@DslMarker`?
<details><summary>Ответ</summary>Он ограничивает неявный доступ к receiver внешнего DSL из вложенной лямбды. Без него легко вызвать метод не того builder'а и получить трудноуловимую ошибку.</details>

## 5. Что сделает `Delegates.vetoable` при `false`?
<details><summary>Ответ</summary>Отклонит присваивание: свойство сохранит старое значение, исключения не будет. `observable` же всегда принимает значение и вызывает колбэк после изменения.</details>

## 6. Можно ли переопределить extension-функцию в наследнике?
<details><summary>Ответ</summary>Нет. Extension-функции разрешаются статически по типу receiver, а не динамически. Для полиморфизма нужен обычный метод класса/интерфейса.</details>

## 7. Что не так с оператором?
```kotlin
operator fun Money.plus(other: Money): Money =
    Money(minor + other.minor, currency)
```
<details><summary>Ответ</summary>Нет проверки валюты: сложение USD и EUR даст бессмысленный результат. Нужен `require(currency == other.currency)`. Также нет защиты от переполнения.</details>

## 8. Когда `with` читается лучше `run`?
<details><summary>Ответ</summary>`with(obj) { ... }` — когда объект уже non-null и нужно выполнить несколько операций над ним, возвращая результат. `run` удобнее для цепочки `obj?.run { ... }` или блока без receiver.</details>
