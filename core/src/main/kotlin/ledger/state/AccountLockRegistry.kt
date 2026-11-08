package ledger.state

import kotlinx.coroutines.sync.Mutex
import ledger.domain.AccountId
import java.util.concurrent.ConcurrentHashMap

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 8 · УПРАЖНЕНИЕ 8.3 — Блокировки по счёту
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Mutex, ConcurrentHashMap, per-key locking
 *  Java-аналогия:  ConcurrentHashMap<AccountId, ReentrantLock>
 *  Тесты:          ./gradlew :core:test -Pmodule=08 --tests "ledger.m08.AccountLockRegistryTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте блокировку по ключу: операции над одним счётом сериализуются,
 *  над разными — выполняются параллельно.
 *
 *  ТРЕБОВАНИЯ
 *  1. withLock(id, block) захватывает Mutex, соответствующий id.
 *  2. Один и тот же Mutex переиспользуется для одного id.
 *  3. Разные счета не блокируют друг друга.
 *  4. Mutex создаётся атомарно (computeIfAbsent), а не getOrPut по обычной Map.
 *  5. withLock возвращает результат block.
 *
 *  ПОДСКАЗКИ
 *  - `ConcurrentHashMap<AccountId, Mutex>()`.
 *  - `locks.computeIfAbsent(id) { Mutex() }.withLock { block() }`.
 *  - Mutex не реентерабелен: не вызывайте withLock на тот же id внутри блока.
 *
 *  ЛОВУШКА
 *  Почему `synchronized` нельзя использовать внутри suspend-функции? Что
 *  произойдёт с потоком при ожидании блокировки?
 * ═══════════════════════════════════════════════════════════
 */
class AccountLockRegistry {

    private val locks = ConcurrentHashMap<AccountId, Mutex>()

    suspend fun <T> withLock(id: AccountId, block: suspend () -> T): T = TODO("m08.3.1")
}
