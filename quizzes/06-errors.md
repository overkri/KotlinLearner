# Квиз 06 — Ошибки и Result

## 1. Скомпилируется ли вызов Java-метода с `throws IOException`?
```kotlin
val text = Files.readString(path)
```
<details><summary>Ответ</summary>Да. В Kotlin нет checked exceptions, компилятор не требует try/catch. Но исключение всё равно может прилететь в рантайме.</details>

## 2. Чем `require` отличается от `error`?
<details><summary>Ответ</summary>`require(condition) { msg }` бросает `IllegalArgumentException` — нарушение предусловия/аргумента. `error(msg)` бросает `IllegalStateException` — общая ошибка состояния.</details>

## 3. Что не так с `Result.failure(TransferError.InsufficientFunds(...))`?
<details><summary>Ответ</summary>`Result.failure` принимает `Throwable`, а `TransferError` — не Throwable. Для доменных ошибок используйте `TransferResult`/собственный sealed-тип или оберните ошибку в исключение (что обычно хуже).</details>

## 4. Что вернёт `runCatching { error("boom") }`?
<details><summary>Ответ</summary>`Result.failure(IllegalStateException("boom"))`. `runCatching` ловит Throwable и упаковывает в failure.</details>

## 5. Почему `runCatching` опасен в suspend-функции?
<details><summary>Ответ</summary>Он ловит `CancellationException`, который сигнализирует об отмене корутины. Если его проглотить, structured concurrency нарушится: родитель не узнает об отмене. Нужно перебрасывать `CancellationException`.</details>

## 6. Что делает `use`?
<details><summary>Ответ</summary>Вызывает `close()` у ресурса после выполнения лямбды, даже если лямбда бросила исключение; возвращает результат лямбды. При исключении в close исходное исключение может быть suppressed.</details>

## 7. Где должна быть транзакция в `TransferEngine`?
<details><summary>Ответ</summary>На уровне хранилища/сервиса, охватывая оба `save` (и запись перевода). В in-memory репозитории её нет; в модуле 12 появится `TxRunner`/`suspendTransaction`, а в модуле 13 — outbox.</details>

## 8. Когда бизнес-ошибку всё же стоит бросать исключением?
<details><summary>Ответ</summary>Когда это нарушение контракта или баг (неверный аргумент, недостижимое состояние), а не ожидаемый сценарий. Ожидаемые отказы (недостаточно средств, заморожен счёт) должны быть значениями, чтобы вызывающий код не забыл их обработать.</details>
