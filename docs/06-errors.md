# 06 — Ошибки и Result

## Цель
Разделить ожидаемые бизнес-отказы и исключительные ситуации: перевод возвращает `TransferResult`, снятие — `Result<Account>`, ресурсы закрываются через `use`.

## Предусловия
Модули 03–05: `TransferRequest`, `TransferResult`, `TransferError`, операторы `Money`.

## Шпаргалка
- В Kotlin **нет checked exceptions**. Java-метод с `throws IOException` можно вызвать без try/catch; для Java-вызывающих используйте `@Throws`.
- Ожидаемая бизнес-ошибка — **значение**: `sealed interface TransferError`, `Result<T>`, `Either`.
- Исключение — для **нарушения контракта** (`require`, `check`, `IllegalStateException`) и инфраструктурных сбоев.
- `Result.success(value)` / `Result.failure(exception)`; `map`, `mapCatching`, `fold`, `getOrElse`, `exceptionOrNull`.
- `runCatching { }` ловит `Throwable`; не используйте его для бизнес-веток и осторожно — вокруг `suspend`-кода.
- `use { }` гарантирует `close()`; если `close` бросит исключение, оно может подавить исходное (см. suppressed exceptions).
- `Result` нельзя использовать как тип свойства/параметра в некоторых API (inline class с ограничениями); для долгого хранения лучше собственный sealed-тип.

## Исключения vs Result
```kotlin
// Бизнес-отказ: это нормальный сценарий, клиент должен его обработать
fun transfer(request: TransferRequest): TransferResult =
    if (source.balance.minor < request.amount.minor) {
        TransferResult.Failure(InsufficientFunds(source.id, source.balance, request.amount))
    } else { ... }

// Нарушение контракта: программист передал невозможные данные
require(amount.minor > 0) { "amount must be positive" }
```
Правило: если вызывающий **обязан** обработать ситуацию — верните значение. Если ситуация означает баг — бросайте исключение.

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `throws IOException` | нет checked exceptions; `@Throws(IOException::class)` для Java |
| `try/catch/finally` | `try/catch/finally` (есть), `use { }` для ресурсов |
| `Optional<T>` | `T?`, `Result<T>` |
| `Either<L,R>` (vavr) | `sealed interface` / `Result` |
| `CompletableFuture.exceptionally` | `Result` + `fold` / корутины |
| `try (var r = ...)` | `r.use { }` |
| `throw new IllegalArgumentException` | `require(...)` |
| `throw new IllegalStateException` | `check(...)` / `error(...)` |

## Транзакционная граница
В `TransferEngine` два `save`. Если второй упадёт, первый уже сохранён — деньги «исчезнут». В in-memory это учебная модель; в модуле 12 появится транзакция БД, а в модуле 13 — outbox. Запомните: **атомарность — свойство хранилища, а не красивого кода**.

## Пять ловушек
1. **`runCatching` вокруг корутин**. Он ловит `CancellationException` и может «проглотить» отмену. В suspend-коде используйте `try/catch` с повторным `throw` для `CancellationException` или `Result` без `runCatching`.
2. **`Result.failure` требует Throwable**. Нельзя вернуть `Result.failure(error: TransferError)`; для доменных ошибок нужен свой sealed-тип или обёртка.
3. **`getOrThrow()` в контроллере**. Это снова превращает ожидаемую ошибку в 500; обрабатывайте `Result`/`TransferResult` явно.
4. **`use` и подавленные исключения**. Если `close()` бросит исключение, исходное станет suppressed; логируйте `Throwable.suppressed`.
5. **Исключение в `init`/`require`**. Оно означает баг вызывающего, а не бизнес-отказ; не ловите его в общем `catch (e: Exception)` без разбора.
