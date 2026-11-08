# Модуль 03 — Типы и модель домена

## Цель
Спроектировать иммутабельную доменную модель LedgerKt: `Money`, идентификаторы, `Account`, статусы и sealed-ошибки. Понять, когда нужны `data class`, `value class`, `enum`, `sealed` и `object`.

## Предусловия
Модули 00–02. В `ledger.domain` уже лежат data-only контракты `Currency`, `Transfer`, `TransferRequest`, `TransferResult`, `LedgerEntry`, `DomainEvent`.

## Шпаргалка
- `data class` — значение: `equals`/`hashCode`/`toString`/`copy`/`componentN` генерируются. Наследоваться от него нельзя.
- `value class` (`@JvmInline`) — typed wrapper без лишней аллокации; идеален для `AccountId`, `TransferId`, `IdempotencyKey`.
- `enum class` — закрытый набор значений с методами и свойствами; отличный кандидат для `AccountStatus`, `Currency`, `Direction`.
- `sealed interface` — закрытая иерархия; `when` проверяется на исчерпываемость. Ошибки и события — значения, а не исключения.
- `object` — singleton; `data object` — singleton с осмысленным `toString`/`equals`.
- `init { require(...) }` — инварианты создаются в конструкторе, а не «где-то потом».
- Иммутабельность: изменения через `copy(...)`, а не сеттеры. `version` — для optimistic locking.
- `companion object` — фабрики (`Money.zero`, `Money.ofMajor`), а не «свалка статики».

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `record Money(long minor, Currency currency)` | `data class Money(val minor: Long, val currency: Currency)` |
| `record` + validate в компактном конструкторе | `init { require(...) }` |
| `new Money(...)` | `Money(...)`; изменения — `money.copy(minor = ...)` |
| `enum Status { ACTIVE, FROZEN }` | `enum class AccountStatus { ACTIVE, FROZEN }` |
| `sealed interface` + `permits` (Java 17) | `sealed interface TransferError` |
| `instanceof` + cast | smart cast в `when (error)` |
| `static final` singleton | `object` / `data object` |
| wrapper `class AccountId { String value; }` | `@JvmInline value class AccountId(val value: String)` |
| `synchronized` setter + mutable entity | immutable `data class` + `copy` + `version` |
| `Objects.requireNonNull` / `if (...) throw` | `require(...) { "message" }` |

## Что важно в этой модели
- **Money нельзя сделать отрицательным**. Направление операции задаёт `Direction`/`Transfer`, а не знак суммы.
- **`Currency` — enum с `fractionDigits`**. JPY не имеет центов; `Money.ofMajor` учитывает это.
- **`Account` иммутабелен**. `freeze()`, `activate()`, `close()`, `withBalance()` возвращают новый объект и увеличивают `version` только при реальном изменении.
- **`TransferError` — sealed**. Клиент API сможет исчерпывающе обработать все ошибки; добавление новой ошибки ломает компиляцию обработчика, а не прод.
- **`DomainEvent` — отдельная sealed-иерархия**. Не путайте ошибку `TransferError.AccountFrozen` с событием `DomainEvent.AccountFrozen`.

## Пять ловушек
1. **`data class` и `var`**. Если сделать `var minor`, `hashCode` изменится после вставки в `HashMap`/`HashSet` — объект «потеряется». Доменные значения должны быть `val`.
2. **`value class` и JVM-стирание**. В `List<AccountId>` value class боксится в объект; выигрыш есть в полях и сигнатурах, но не всегда в коллекциях.
3. **`copy` не глубокий**. Вложенные mutable-объекты (например, `MutableList`) останутся общими; для домена используйте только read-only-коллекции.
4. **`when` по `sealed` без `else`**. Это преимущество, а не баг: новая ошибка заставит обновить обработчик. Не добавляйте `else` «на всякий случай» — потеряете проверку.
5. **`init` и `require`**. Валидация в `init` выполняется и для `copy(...)`, поэтому `copy(minor = -1)` тоже упадёт — это то, что нужно.
