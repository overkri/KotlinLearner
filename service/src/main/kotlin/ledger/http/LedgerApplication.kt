package ledger.http

import io.ktor.server.application.Application

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 10 · УПРАЖНЕНИЕ 10.3 — Ktor-приложение
 * ═══════════════════════════════════════════════════════════
 *  Тема:           ContentNegotiation, StatusPages, routing, receive/respond
 *  Java-аналогия:  @RestController + @ControllerAdvice + @RequestBody
 *  Тесты:          ./gradlew :service:test -Pmodule=10 --tests "ledger.m10.LedgerApplicationTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Соберите HTTP API LedgerKt на Ktor.
 *
 *  ТРЕБОВАНИЯ
 *  1. Установить ContentNegotiation с ledgerJson().
 *  2. Установить StatusPages:
 *     - BadRequestException/ContentTransformationException -> 400 ErrorDto("bad_request", сообщение);
 *     - остальные Throwable -> 500 ErrorDto("internal_error", "Internal server error").
 *  3. GET /health -> 200 {"status":"UP"}.
 *  4. GET /accounts -> 200 List<AccountDto>.
 *  5. GET /accounts/{id} -> 200 AccountDto или 404 ErrorDto("account_not_found", ...).
 *  6. POST /transfers:
 *     - принять TransferRequestDto;
 *     - невалидный DTO -> 400 ErrorDto("invalid_request", сообщение);
 *     - Success -> 201 TransferResponseDto;
 *     - Failure -> toHttpStatus() + ErrorDto(code, message).
 *  7. Неизвестный путь -> 404.
 *
 *  ПОДСКАЗКИ
 *  - `install(ContentNegotiation) { json(ledgerJson()) }`.
 *  - `call.receive<TransferRequestDto>()`, `call.respond(status, body)`.
 *  - `call.parameters["id"]`.
 *  - `dto.toDomain().getOrElse { return@post call.respond(...) }` — ранний ответ.
 *
 *  ЛОВУШКА
 *  Почему нельзя просто пробросить IllegalArgumentException из `toDomain()`
 *  в StatusPages и вернуть 500? Что увидит клиент и что попадёт в логи?
 * ═══════════════════════════════════════════════════════════
 */
fun Application.ledgerModule(dependencies: LedgerDependencies): Unit = TODO("m10.3.1")
