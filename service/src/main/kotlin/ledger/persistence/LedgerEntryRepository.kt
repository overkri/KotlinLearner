package ledger.persistence

import ledger.domain.AccountId
import ledger.domain.LedgerEntry
import javax.sql.DataSource

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 12 · УПРАЖНЕНИЕ 12.6 — N+1
 * ═══════════════════════════════════════════════════════════
 *  Тема:           batch loading, IN (...), N+1 problem
 *  Java-аналогия:  JPA @EntityGraph / JOIN FETCH
 *  Тесты:          ./gradlew :service:test -Pmodule=12 --tests "ledger.m12.LedgerEntryRepositoryTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Загрузите проводки для набора счетов одним запросом.
 *
 *  ТРЕБОВАНИЯ
 *  1. Один SQL-запрос с IN (?, ?, ...), а не запрос на каждый accountId.
 *  2. Вернуть Map<AccountId, List<LedgerEntry>> для всех запрошенных счетов;
 *     для счетов без проводок — пустой список.
 *  3. Пустой вход -> пустая Map без запроса к БД.
 *  4. Сортировка проводок внутри счёта — по id возрастанию.
 *  5. Маппинг Instant из TIMESTAMP.
 *
 *  ПОДСКАЗКИ
 *  - `SELECT ... WHERE account_id IN (${ids.joinToString(",") { "?" }}) ORDER BY id`.
 *  - Устанавливайте параметры через PreparedStatement.
 *  - Тест QueryCounter проверяет, что prepareStatement вызван один раз.
 *
 *  ЛОВУШКА
 *  Почему N+1 незаметен на 10 счетах и убивает БД на 10 000? Как бы вы
 *  нашли его в проде (метрики, pg_stat_statements, лог SQL)?
 * ═══════════════════════════════════════════════════════════
 */
class JdbcLedgerEntryRepository(
    private val dataSource: DataSource,
) {
    fun findByAccountIds(accountIds: List<AccountId>): Map<AccountId, List<LedgerEntry>> =
        TODO("m12.6.1")
}
