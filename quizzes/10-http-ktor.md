# Квиз 10 — Голый HTTP → Ktor

## 1. Чем Ktor routing отличается от Spring MVC?
<details><summary>Ответ</summary>В Ktor маршруты — обычные вызовы DSL `routing { get("/x") { ... } }`; нет аннотаций и сканирования. Плагины устанавливаются явно, поэтому видно, что именно происходит.</details>

## 2. Как получить path-параметр `{id}`?
<details><summary>Ответ</summary>`call.parameters["id"]` внутри обработчика. В Spring это был бы `@PathVariable("id")`.</details>

## 3. Что делает `ContentNegotiation`?
<details><summary>Ответ</summary>Выбирает формат (JSON и т.п.) по `Content-Type`/`Accept` и сериализует/десериализует тела. В Spring это делают `HttpMessageConverter`-ы.</details>

## 4. Зачем `StatusPages`?
<details><summary>Ответ</summary>Централизованно обрабатывает исключения и возвращает клиенту единый формат ошибки (`ErrorDto`), аналог `@ControllerAdvice`.</details>

## 5. Какой код вернуть на невалидный JSON?
<details><summary>Ответ</summary>400 Bad Request. Это ошибка формата запроса, а не сбой сервера. 500 допустим только для неожиданных исключений.</details>

## 6. Чем `testApplication` лучше запуска реального сервера?
<details><summary>Ответ</summary>Он быстрее, не занимает порт, изолирует тесты и позволяет проверить маршруты/плагины. Реальный `embeddedServer` нужен для smoke/E2E.</details>

## 7. Почему `Account` не отдаётся напрямую?
<details><summary>Ответ</summary>Доменный объект может не иметь сериализатора, содержать лишние поля/инварианты и менять контракт при рефакторинге. DTO фиксирует API независимо от домена.</details>

## 8. Что вернёт `POST /transfers` при `InsufficientFunds`?
<details><summary>Ответ</summary>409 Conflict с `ErrorDto(code = "insufficient_funds", message = ...)`. Клиент может показать понятную ошибку и не повторять запрос вслепую.</details>
