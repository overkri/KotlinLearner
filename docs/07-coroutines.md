# 07 — Корутины

## Цель
Писать асинхронный код без `CompletableFuture`: `suspend`-функции, structured concurrency, ретраи, таймауты, отмена и параллельная загрузка.

## Предусловия
Модули 00–06. `TransferClient`, `AccountRepository`, `TransferResult` готовы.

## Шпаргалка
- `suspend fun` — функция, которая может приостанавливаться, не блокируя поток. Компилируется в метод с `Continuation`.
- Корутина запускается в `CoroutineScope`: `launch` (fire-and-forget), `async` (результат `Deferred`).
- **Structured concurrency**: родитель ждёт детей; падение ребёнка отменяет siblings (в `coroutineScope`). `supervisorScope` изолирует падения.
- `runBlocking` — только для `main`/тестов/CLI; в серверном коде не использовать.
- `GlobalScope` — утечка; в эталонном коде запрещён.
- `delay(ms)` — suspend-аналог `Thread.sleep`, поддерживает отмену и виртуальное время тестов.
- `withContext(Dispatchers.IO)` — для блокирующих I/O (JDBC, файлы, legacy-клиенты).
- `withTimeout`/`withTimeoutOrNull` — отмена по таймауту.
- `CancellationException` — не бизнес-ошибка, а сигнал отмены; его нельзя глотать.
- `Mutex` — suspend-аналог `synchronized`; `Atomic*` — для простых счётчиков; `StateFlow` — для состояния.
- `runTest` + `testScheduler.currentTime` — виртуальное время, без реальных задержек.

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `CompletableFuture<T>` | `suspend fun` / `Deferred<T>` |
| `CompletableFuture.allOf` | `coroutineScope { list.map { async { ... } }.awaitAll() }` |
| `ExecutorService` | `Dispatchers.Default` / `Dispatchers.IO` |
| `Thread.sleep` | `delay` |
| `Future.cancel` | `Job.cancel()` + cooperative checks |
| `synchronized` | `Mutex.withLock` |
| `AtomicInteger` | `AtomicInteger` / `Mutex` / `StateFlow` |
| `@Async` | `launch` / `async` |
| `@Scheduled` | `while (isActive) { ...; delay(...) }` |
| Spring Retry | собственный `retry` / Resilience4j (упоминание) |

## Structured concurrency
```kotlin
suspend fun loadAccounts(ids: List<AccountId>, fetcher: suspend (AccountId) -> Account) =
    coroutineScope {
        ids.map { id -> async { fetcher(id) } }.awaitAll()
    }
```
- `coroutineScope` ждёт всех детей; при падении одного отменяет остальных и пробрасывает ошибку.
- `supervisorScope` не отменяет siblings; каждый `async` нужно обернуть в `Result`.
- `awaitAll` сохраняет порядок создания `Deferred`, а не порядок завершения.

## Пять ловушек
1. **`GlobalScope.launch`**. Корутина живёт вне жизненного цикла приложения, её никто не отменит и не дождётся. Используйте scope приложения/запроса.
2. **`Thread.sleep` в `suspend`-функции**. Блокирует поток; в тестах ломает виртуальное время. Используйте `delay`.
3. **Проглоченный `CancellationException`**. `runCatching`/`catch (e: Exception)` без перебрасывания отмены ломает structured concurrency.
4. **`runBlocking` в контроллере**. Блокирует поток event loop; используйте `suspend`-функции и `call.respond`.
5. **Параллелизм без ограничения**. `ids.map { async { ... } }` на 100k элементов создаст 100k корутин; используйте `Semaphore`/`chunked`/пул.
