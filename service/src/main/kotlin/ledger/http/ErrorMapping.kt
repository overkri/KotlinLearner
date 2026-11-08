package ledger.http

import io.ktor.http.HttpStatusCode
import ledger.domain.TransferError

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 10 · УПРАЖНЕНИЕ 10.2 — Доменные ошибки → HTTP
 * ═══════════════════════════════════════════════════════════
 *  Тема:           when по sealed, HTTP status mapping
 *  Java-аналогия:  @ExceptionHandler + ResponseEntity.status
 *  Тесты:          ./gradlew :service:test -Pmodule=10 --tests "ledger.m10.ErrorMappingTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Сопоставьте бизнес-ошибки с HTTP-кодами.
 *
 *  ТРЕБОВАНИЯ
 *  1. AccountNotFound -> 404 Not Found.
 *  2. AccountFrozen -> 409 Conflict.
 *  3. InsufficientFunds -> 409 Conflict.
 *  4. CurrencyMismatch -> 422 Unprocessable Entity.
 *  5. InvalidAmount -> 400 Bad Request.
 *  6. SameAccount -> 400 Bad Request.
 *
 *  ПОДСКАЗКИ
 *  - `HttpStatusCode.NotFound`, `Conflict`, `UnprocessableEntity`, `BadRequest`.
 *  - `when (this)` по sealed не требует else.
 *
 *  ЛОВУШКА
 *  Почему 409 для замороженного счёта лучше 400? Что клиент может сделать
 *  с 409 и не может с 500?
 * ═══════════════════════════════════════════════════════════
 */
fun TransferError.toHttpStatus(): HttpStatusCode = TODO("m10.2.1")
