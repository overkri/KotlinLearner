package ledger.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ledger.domain.Account
import ledger.domain.AccountId

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 7 · УПРАЖНЕНИЕ 7.3 — Параллельная загрузка
 * ═══════════════════════════════════════════════════════════
 *  Тема:           coroutineScope, async, awaitAll, structured concurrency
 *  Java-аналогия:  CompletableFuture.allOf + join
 *  Тесты:          ./gradlew :core:test -Pmodule=07 --tests "ledger.m07.ParallelAggregatorTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Загрузите счета параллельно, сохранив порядок результатов.
 *
 *  ТРЕБОВАНИЯ
 *  1. Каждый id загружается в отдельной корутине.
 *  2. Результаты идут в порядке ids, а не в порядке завершения.
 *  3. Если fetcher бросил исключение, корутина-родитель отменяет остальных
 *     и пробрасывает исключение (structured concurrency).
 *  4. Использовать coroutineScope + async + awaitAll.
 *
 *  ПОДСКАЗКИ
 *  - `coroutineScope { ids.map { id -> async { fetcher(id) } }.awaitAll() }`.
 *  - awaitAll ждёт все Deferred и возвращает List в порядке создания.
 *  - Не запускайте `GlobalScope.launch` — это утечка.
 *
 *  ЛОВУШКА
 *  Чем `coroutineScope` отличается от `supervisorScope` при падении одной
 *  дочерней корутины? Какой выбрать для «загрузить всё или упасть»?
 * ═══════════════════════════════════════════════════════════
 */
suspend fun loadAccounts(
    ids: List<AccountId>,
    fetcher: suspend (AccountId) -> Account,
): List<Account> = TODO("m07.3.1")
