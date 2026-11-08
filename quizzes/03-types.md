# Квиз 03 — Типы и модель домена

## 1. Что сгенерирует `data class Money(val minor: Long, val currency: Currency)`?
<details><summary>Ответ</summary>`equals`, `hashCode`, `toString`, `copy`, `component1/component2`. Наследоваться от `Money` нельзя, если класс не `open` (а `data class` открытым быть не может).</details>

## 2. Чем `value class AccountId(val value: String)` отличается от обычного wrapper-класса?
<details><summary>Ответ</summary>В большинстве случаев компилятор представляет его как обычный `String` без создания объекта. Типобезопасность сохраняется, аллокации — нет. В коллекциях/дженериках возможен boxing.</details>

## 3. Скомпилируется ли?
```kotlin
val money = Money(100, Currency.USD)
val changed = money.copy(minor = -1)
```
<details><summary>Ответ</summary>Скомпилируется, но в рантайме `copy` вызовет конструктор и `init`, поэтому `require(minor >= 0)` бросит `IllegalArgumentException`. Инварианты защищают и копирование.</details>

## 4. Почему `Account` сделан иммутабельным?
<details><summary>Ответ</summary>Иммутабельный объект безопасно передавать между потоками и корутинами; изменения явны (`copy`), а `version` позволяет реализовать optimistic locking. Сеттеры и `var` сделали бы гонки незаметными.</details>

## 5. Что вернёт `AccountStatus.CLOSED.canTransitionTo(AccountStatus.ACTIVE)`?
<details><summary>Ответ</summary>`false`. CLOSED — терминальный статус; переходы из него запрещены. Метод-предикат не бросает исключение.</details>

## 6. Зачем `TransferError` sealed, а не открытая иерархия исключений?
<details><summary>Ответ</summary>Ошибки — ожидаемые бизнес-результаты, их удобно возвращать как значения и обрабатывать `when` без `else`. Компилятор проверяет полноту, а исключения не являются частью сигнатуры.</details>

## 7. Чем `object` отличается от `companion object`?
<details><summary>Ответ</summary>`object` — самостоятельный singleton. `companion object` — singleton, вложенный в класс и доступный через имя класса (`Money.zero(...)`). В Java это примерно статический вложенный класс с `INSTANCE`, но с доступом к приватным членам внешнего класса.</details>

## 8. Что не так с этим кодом?
```kotlin
data class Account(var balance: Money, var version: Long)
```
<details><summary>Ответ</summary>`var` нарушает иммутабельность и делает `hashCode` нестабильным; изменение баланса не контролируется инвариантами. Нужны `val` и методы `withBalance(...)`, возвращающие копию с увеличенным `version`.</details>
