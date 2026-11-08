package ledger.persistence

import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountRepository
import org.jetbrains.exposed.v1.jdbc.Database

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 12 · УПРАЖНЕНИЕ 12.3 — Exposed-репозиторий
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Exposed DSL, ResultRow, insert/update/selectAll
 *  Java-аналогия:  JPA repository, но с явным SQL-DSL
 *  Тесты:          ./gradlew :service:test -Pmodule=12 --tests "ledger.m12.ExposedAccountRepositoryTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте тот же AccountRepository на Exposed DSL.
 *
 *  ТРЕБОВАНИЯ
 *  1. findById через transaction(database) + AccountsTable.selectAll().where { id eq ... }.
 *  2. save: version == 0 -> insert; иначе update с условием
 *     (id eq ...) and (version eq account.version - 1).
 *  3. UPDATE 0 строк -> OptimisticLockException.
 *  4. Маппинг ResultRow -> Account в отдельной функции.
 *  5. Не использовать raw SQL.
 *
 *  ПОДСКАЗКИ
 *  - Импорты: `org.jetbrains.exposed.v1.jdbc.transactions.transaction`,
 *    `org.jetbrains.exposed.v1.jdbc.selectAll`, `insert`, `update`,
 *    `org.jetbrains.exposed.v1.core.eq`, `and`.
 *  - `AccountsTable.selectAll().where { AccountsTable.id eq id.value }.singleOrNull()`.
 *  - `update({ ... }) { it[column] = value }` возвращает количество строк.
 *
 *  ЛОВУШКА
 *  Почему Exposed DSL всё равно требует понимания SQL? Где вы будете искать
 *  N+1 и какие запросы он генерирует?
 * ═══════════════════════════════════════════════════════════
 */
class ExposedAccountRepository(
    private val database: Database,
) : AccountRepository {

    override fun findById(id: AccountId): Account? = TODO("m12.3.1")

    override fun save(account: Account): Unit = TODO("m12.3.2")
}
