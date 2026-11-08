package ledger.springbridge

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 14 · УПРАЖНЕНИЕ 14.1 — Spring-сервис
 * ═══════════════════════════════════════════════════════════
 *  Тема:           @Service, @Transactional, JpaRepository, Kotlin + Spring
 *  Java-аналогия:  тот же сервис на Java, но с явными getter/setter
 *  Тесты:          ./gradlew :spring-bridge:test -Pmodule=14 --tests "ledger.m14.AccountControllerTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте сервис над AccountJpaRepository.
 *
 *  ТРЕБОВАНИЯ
 *  1. find(id): AccountEntity?; readOnly-транзакция; null, если нет.
 *  2. freeze(id): null, если счёта нет; CLOSED -> IllegalArgumentException
 *     "closed account cannot be frozen"; FROZEN -> вернуть как есть;
 *     ACTIVE -> status = "FROZEN", version += 1, save.
 *  3. Использовать @Transactional (Spring proxy), а не ручной commit.
 *  4. Не менять id и ownerName при freeze.
 *
 *  ПОДСКАЗКИ
 *  - `repository.findById(id).orElse(null)` — Java Optional.
 *  - В Kotlin `lateinit`/`var` у сущности допустимы: JPA-сущность — mutable.
 *  - `version` увеличивается вручную; в JPA можно было бы @Version.
 *
 *  ЛОВУШКА
 *  Почему `@Transactional` на приватном методе или вызове из того же класса
 *  не сработает? Как Spring создаёт proxy?
 * ═══════════════════════════════════════════════════════════
 */
@Service
class SpringAccountService(
    private val repository: AccountJpaRepository,
) {
    @Transactional(readOnly = true)
    fun find(id: String): AccountEntity? = TODO("m14.1.1")

    @Transactional
    fun freeze(id: String): AccountEntity? = TODO("m14.1.2")
}
