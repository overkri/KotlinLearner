package ledger.persistence

import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountRepository
import java.sql.Connection
import java.sql.ResultSet
import javax.sql.DataSource

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 12 · УПРАЖНЕНИЕ 12.2 — JDBC-репозиторий
 * ═══════════════════════════════════════════════════════════
 *  Тема:           raw JDBC, PreparedStatement, optimistic locking
 *  Java-аналогия:  JdbcTemplate + RowMapper + @Version
 *  Тесты:          ./gradlew :service:test -Pmodule=12 --tests "ledger.m12.JdbcAccountRepositoryTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте AccountRepository на чистом JDBC. Никаких ORM.
 *
 *  ТРЕБОВАНИЯ
 *  1. findById: SELECT по id; null, если нет; маппинг всех полей.
 *  2. save: version == 0 -> INSERT; иначе UPDATE ... WHERE id = ? AND version = ?,
 *     где ожидаемая старая версия = account.version - 1.
 *  3. Если UPDATE обновил 0 строк — OptimisticLockException с id.
 *  4. Использовать PreparedStatement и `use` (Connection/Statement закрываются).
 *  5. Не конкатенировать значения в SQL.
 *
 *  ПОДСКАЗКИ
 *  - `dataSource.connection.use { connection -> ... }`.
 *  - `connection.prepareStatement(sql).use { ps -> ps.setString(1, ...) }`.
 *  - Для UPDATE проверьте `executeUpdate() == 1`.
 *  - `ResultSet.getString("status")` -> AccountStatus.valueOf.
 *
 *  ЛОВУШКА
 *  Почему `UPDATE ... WHERE id = ?` без version опасен при конкурентных
 *  переводах? Что произойдёт с балансом при lost update?
 * ═══════════════════════════════════════════════════════════
 */
class JdbcAccountRepository(
    private val dataSource: DataSource,
) : AccountRepository {

    override fun findById(id: AccountId): Account? = TODO("m12.2.1")

    override fun save(account: Account): Unit = TODO("m12.2.2")
}
