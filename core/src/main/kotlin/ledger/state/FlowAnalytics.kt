package ledger.state

import kotlinx.coroutines.flow.Flow
import ledger.domain.Currency
import ledger.domain.LedgerEntry
import ledger.domain.Transfer

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 8 · УПРАЖНЕНИЕ 8.5 — Flow-аналитика
 * ═══════════════════════════════════════════════════════════
 *  Тема:           runningFold, filter, Flow как cold stream
 *  Java-аналогия:  Reactor Flux.scan / filter
 *  Тесты:          ./gradlew :core:test -Pmodule=08 --tests "ledger.m08.FlowAnalyticsTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Постройте два Flow-преобразования для отчётов.
 *
 *  ТРЕБОВАНИЯ
 *  1. totalsByCurrencyFlow(): эмитит накопленные знаковые суммы по валютам
 *     после каждого элемента, начиная с пустой Map.
 *  2. completedOnly(): оставляет только Transfer со статусом COMPLETED.
 *  3. Flow должен быть холодным: каждый collect заново вычисляет цепочку.
 *
 *  ПОДСКАЗКИ
 *  - `runningFold(emptyMap<Currency, Long>()) { acc, entry -> ... }`.
 *  - `filter { it.status == TransferStatus.COMPLETED }`.
 *  - signedMinor из ledger.collections (DEBIT минус, CREDIT плюс).
 *
 *  ЛОВУШКА
 *  Чем Flow отличается от SharedFlow по «холодности» и по количеству
 *  выполнений upstream при двух collect?
 * ═══════════════════════════════════════════════════════════
 */
fun Flow<LedgerEntry>.totalsByCurrencyFlow(): Flow<Map<Currency, Long>> = TODO("m08.5.1")

fun Flow<Transfer>.completedOnly(): Flow<Transfer> = TODO("m08.5.2")
