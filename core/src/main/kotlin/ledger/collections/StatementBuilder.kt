package ledger.collections

import ledger.domain.AccountId
import ledger.domain.LedgerEntry

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 4 · УПРАЖНЕНИЕ 4.1 — Выписка по счёту
 * ═══════════════════════════════════════════════════════════
 *  Тема:           filter, sortedWith, mapIndexed/fold, running balance
 *  Java-аналогия:  stream().filter().sorted().map() + цикл
 *  Тесты:          ./gradlew :core:test -Pmodule=04 --tests "ledger.m04.StatementBuilderTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Постройте выписку по одному счёту с накопительным остатком.
 *
 *  ТРЕБОВАНИЯ
 *  1. Оставьте только проводки accountId.
 *  2. Отсортируйте по createdAt, при равенстве — по id.
 *  3. DEBIT уменьшает остаток, CREDIT увеличивает.
 *  4. index в StatementRow начинается с 1 и идёт по порядку.
 *  5. balanceAfterMinor — знаковый остаток (может быть отрицательным).
 *  6. Если у проводок счёта разные валюты — IllegalArgumentException
 *     "mixed currencies in statement".
 *  7. Пустой результат — пустой список.
 *
 *  ПОДСКАЗКИ
 *  - `filter { it.accountId == accountId }`.
 *  - `sortedWith(compareBy({ it.createdAt }, { it.id }))`.
 *  - Накопление: `var balance = 0L` + `forEachIndexed`/`mapIndexed`.
 *  - Знак: `if (entry.direction == Direction.DEBIT) -entry.amount.minor else entry.amount.minor`.
 *
 *  ЛОВУШКА
 *  Почему `sortedBy { it.createdAt }` может дать нестабильный порядок при
 *  одинаковых timestamp и как это влияет на воспроизводимость выписки?
 * ═══════════════════════════════════════════════════════════
 */
fun statement(entries: List<LedgerEntry>, accountId: AccountId): List<StatementRow> =
    TODO("m04.1.1")
