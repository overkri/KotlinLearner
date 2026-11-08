# Квиз 07 — Корутины

## 1. Чем `suspend`-функция отличается от обычной?
<details><summary>Ответ</summary>Она может приостанавливаться без блокировки потока: компилятор добавляет `Continuation`, а вызов возможен только из корутины или другой `suspend`-функции. Обычная функция не может вызвать suspend-функцию напрямую.</details>

## 2. Что выведет?
```kotlin
runBlocking {
    launch { delay(100); println("child") }
    println("parent")
}
```
<details><summary>Ответ</summary>`parent`, затем `child`. `launch` не блокирует; `runBlocking` ждёт завершения дочерней корутины.</details>

## 3. Чем `coroutineScope` отличается от `supervisorScope`?
<details><summary>Ответ</summary>В `coroutineScope` падение ребёнка отменяет остальных и пробрасывает ошибку. В `supervisorScope` падение ребёнка не отменяет siblings; ошибку нужно обработать в самом ребёнке (например, `Result.failure`).</details>

## 4. Что не так с этим кодом?
```kotlin
suspend fun load() {
    runCatching { api.call() }
}
```
<details><summary>Ответ</summary>`runCatching` ловит `CancellationException` и проглатывает отмену. Нужно перебрасывать его или использовать try/catch с отдельной веткой для отмены.</details>

## 5. Почему `Thread.sleep(1000)` плох в `suspend`-функции?
<details><summary>Ответ</summary>Он блокирует поток на 1 секунду, не отдавая его другим корутинам; в `runTest` он не использует виртуальное время, тест реально спит. Нужен `delay(1000)`.</details>

## 6. Что вернёт `withTimeoutOrNull(100) { delay(1000); 42 }`?
<details><summary>Ответ</summary>`null`: блок отменён по таймауту. `withTimeout` вместо этого бросил бы `TimeoutCancellationException`.</details>

## 7. Зачем `withContext(Dispatchers.IO)` вокруг JDBC-вызова?
<details><summary>Ответ</summary>JDBC блокирует поток. `Dispatchers.IO` выделяет пул для блокирующих операций, чтобы не блокировать `Dispatchers.Default`/event loop. Для чистой CPU-работы — `Dispatchers.Default`.</details>

## 8. Что произойдёт с `async`-детьми, если родитель бросит исключение в `supervisorScope`?
<details><summary>Ответ</summary>Они будут отменены: `supervisorScope` изолирует падения детей друг от друга, но не отмену родителя. Если исключение в теле `supervisorScope`, все дети отменяются.</details>
