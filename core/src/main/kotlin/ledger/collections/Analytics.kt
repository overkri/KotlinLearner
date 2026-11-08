package ledger.collections

import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.LedgerEntry
import ledger.domain.Money

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 4 · УПРАЖНЕНИЕ 4.2 — Аналитика проводок
 * ═══════════════════════════════════════════════════════════
 *  Тема:           groupBy, sumOf, sortedByDescending, take, average
 *  Java-аналогия:  Collectors.groupingBy + summingLong, sorted, limit
 *  Тесты:          ./gradlew :core:test -Pmodule=04 --tests "ledger.m04.AnalyticsTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Посчитайте три отчёта по списку проводок.
 *
 *  ТРЕБОВАНИЯ
 *  1. totalsByCurrency: знаковая сумма (CREDIT +, DEBIT -) по каждой валюте.
 *     Пустой список -> пустая Map.
 *  2. busiestAccounts(entries, limit): количество проводок по счёту;
 *     сортировка по убыванию количества, при равенстве — по AccountId.value;
 *     вернуть не больше limit. limit < 0 -> IllegalArgumentException
 *     "limit must be non-negative".
 *  3. averageDebit(entries): среднее арифметическое amount.minor только для DEBIT
 *     (целочисленное деление); null, если DEBIT нет; разные валюты ->
 *     IllegalArgumentException "mixed currencies".
 *
 *  ПОДСКАЗКИ
 *  - `groupBy { it.amount.currency }.mapValues { (_, group) -> group.sumOf(::signedMinor) }`.
 *  - `sortedWith(compareByDescending<Pair<AccountId, Int>> { it.second }.thenBy { it.first.value })`.
 *  - `take(limit)` безопасен и для limit больше размера.
 *
 *  ЛОВУШКА
 *  Чем `sumOf` отличается от `fold` по типу аккумулятора и когда возможен
 *  overflow Long?
 * ═══════════════════════════════════════════════════════════
 */
fun totalsByCurrency(entries: List<LedgerEntry>): Map<Currency, Long> = TODO("m04.2.1")

fun busiestAccounts(entries: List<LedgerEntry>, limit: Int): List<Pair<AccountId, Int>> =
    TODO("m04.2.2")

fun averageDebit(entries: List<LedgerEntry>): Money? = TODO("m04.2.3")

internal fun signedMinor(entry: LedgerEntry): Long =
    if (entry.direction == ledger.domain.Direction.DEBIT) -entry.amount.minor else entry.amount.minor
