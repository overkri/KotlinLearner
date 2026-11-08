package ledger.persistence

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 12 · УПРАЖНЕНИЕ 12.4 — Транзакции
 * ═══════════════════════════════════════════════════════════
 *  Тема:           suspendTransaction, rollback, атомарность
 *  Java-аналогия:  @Transactional
 *  Тесты:          ./gradlew :service:test -Pmodule=12 --tests "ledger.m12.TxRunnerTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте suspend-обёртку над транзакцией Exposed.
 *
 *  ТРЕБОВАНИЯ
 *  1. tx(block) выполняет block в транзакции database.
 *  2. При исключении — rollback и проброс исключения.
 *  3. При успехе — commit.
 *  4. Использовать suspendTransaction (не blocking transaction) и не блокировать поток.
 *  5. Возвращать результат block.
 *
 *  ПОДСКАЗКИ
 *  - `suspendTransaction(database) { block() }` из
 *    `org.jetbrains.exposed.v1.jdbc.transactions`.
 *  - Receiver — JdbcTransaction; внутри доступны insert/selectAll.
 *  - Не глотайте CancellationException.
 *
 *  ЛОВУШКА
 *  Почему вложенный `suspendTransaction` может создать второе соединение и
 *  как это связано с размером пула? Когда нужен `newSuspendedTransaction`?
 * ═══════════════════════════════════════════════════════════
 */
class TxRunner(
    private val database: Database,
) {
    suspend fun <T> tx(block: suspend JdbcTransaction.() -> T): T = TODO("m12.4.1")
}
