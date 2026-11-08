package ledger.collections

import ledger.domain.Account
import ledger.domain.AccountId

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 4 · УПРАЖНЕНИЕ 4.4 — Батчи, индексы, партиции
 * ═══════════════════════════════════════════════════════════
 *  Тема:           chunked, associateBy, partition, destructuring
 *  Java-аналогия:  Lists.partition, Collectors.toMap, partitioningBy
 *  Тесты:          ./gradlew :core:test -Pmodule=04 --tests "ledger.m04.BatchIndexTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Подготовьте счета к пакетной обработке.
 *
 *  ТРЕБОВАНИЯ
 *  1. chunkIds(ids, size): разбить на списки размера не больше size;
 *     size <= 0 -> IllegalArgumentException "size must be positive";
 *     пустой вход -> пустой список.
 *  2. indexById(accounts): Map<AccountId, Account>; при дубликате id
 *     побеждает последний элемент (поведение associateBy).
 *  3. partitionByStatus(accounts): Pair(ACTIVE, все остальные), порядок
 *     элементов внутри частей сохраняется.
 *
 *  ПОДСКАЗКИ
 *  - `ids.chunked(size)` возвращает List<List<T>> и сам проверяет size? Нет,
 *     поэтому require(size > 0).
 *  - `accounts.associateBy { it.id }`.
 *  - `accounts.partition { it.status == AccountStatus.ACTIVE }`.
 *
 *  ЛОВУШКА
 *  Что произойдёт с порядком ключей в `associateBy` и почему нельзя
 *  полагаться на порядок `Map` при сериализации?
 * ═══════════════════════════════════════════════════════════
 */
fun chunkIds(ids: List<AccountId>, size: Int): List<List<AccountId>> = TODO("m04.4.1")

fun indexById(accounts: List<Account>): Map<AccountId, Account> = TODO("m04.4.2")

fun partitionByStatus(accounts: List<Account>): Pair<List<Account>, List<Account>> =
    TODO("m04.4.3")
