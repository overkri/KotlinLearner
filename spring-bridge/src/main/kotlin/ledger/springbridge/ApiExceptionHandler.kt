package ledger.springbridge

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 14 · УПРАЖНЕНИЕ 14.3 — ExceptionHandler
 * ═══════════════════════════════════════════════════════════
 *  Тема:           @RestControllerAdvice, @ExceptionHandler, ResponseEntity
 *  Java-аналогия:  @ControllerAdvice
 *  Тесты:          ./gradlew :spring-bridge:test -Pmodule=14 --tests "ledger.m14.AccountControllerTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Верните единый формат ошибок API.
 *
 *  ТРЕБОВАНИЯ
 *  1. AccountNotFoundException -> 404 ErrorResponse("account_not_found", message).
 *  2. IllegalArgumentException -> 400 ErrorResponse("invalid_request", message).
 *  3. Остальные исключения не перехватывать (пусть Spring вернёт 500).
 *  4. Использовать ResponseEntity.status(...) или ResponseEntity.badRequest().
 *
 *  ПОДСКАЗКИ
 *  - `ResponseEntity.status(HttpStatus.NOT_FOUND).body(...)`.
 *  - `error.message ?: "..."` — сообщение может быть null.
 *  - Не логируйте stack trace с секретами; для 4xx достаточно message.
 *
 *  ЛОВУШКА
 *  Почему нельзя отдавать клиенту `exception.stackTrace` или текст SQL?
 *  Какие данные утекают в 500-ответе?
 * ═══════════════════════════════════════════════════════════
 */
@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleNotFound(error: AccountNotFoundException): ResponseEntity<ErrorResponse> =
        TODO("m14.3.1")

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(error: IllegalArgumentException): ResponseEntity<ErrorResponse> =
        TODO("m14.3.2")
}
