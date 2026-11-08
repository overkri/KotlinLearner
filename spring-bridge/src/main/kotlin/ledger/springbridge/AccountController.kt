package ledger.springbridge

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 14 · УПРАЖНЕНИЕ 14.2 — Spring REST controller
 * ═══════════════════════════════════════════════════════════
 *  Тема:           @RestController, @GetMapping/@PostMapping, DTO
 *  Java-аналогия:  тот же контроллер на Java
 *  Тесты:          ./gradlew :spring-bridge:test -Pmodule=14 --tests "ledger.m14.AccountControllerTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте read/freeze API поверх SpringAccountService.
 *
 *  ТРЕБОВАНИЯ
 *  1. GET /api/accounts/{id} -> 200 AccountResponse или AccountNotFoundException.
 *  2. POST /api/accounts/{id}/freeze -> 200 AccountResponse.
 *  3. Не отдавать AccountEntity напрямую — только AccountResponse.
 *  4. Использовать toResponse().
 *
 *  ПОДСКАЗКИ
 *  - `service.find(id) ?: throw AccountNotFoundException(id)`.
 *  - `@PathVariable id: String`.
 *  - Ошибки обработает @RestControllerAdvice (упражнение 14.3).
 *
 *  ЛОВУШКА
 *  Почему Entity нельзя отдавать из контроллера? Что будет с lazy-полями
 *  и с контрактом API при изменении схемы БД?
 * ═══════════════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val service: SpringAccountService,
) {
    @GetMapping("/{id}")
    fun find(@PathVariable id: String): AccountResponse = TODO("m14.2.1")

    @PostMapping("/{id}/freeze")
    fun freeze(@PathVariable id: String): AccountResponse = TODO("m14.2.2")
}
