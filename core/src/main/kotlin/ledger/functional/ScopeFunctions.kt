package ledger.functional

import ledger.domain.Account
import ledger.domain.Transfer

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 5 · УПРАЖНЕНИЕ 5.3 — Scope-функции
 * ═══════════════════════════════════════════════════════════
 *  Тема:           let, run, with, apply, also, takeIf
 *  Java-аналогия:  Optional.map / if (x != null) ...
 *  Тесты:          ./gradlew :core:test -Pmodule=05 --tests "ledger.m05.ScopeFunctionsTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Используйте scope-функции там, где они делают код чище, а не просто
 *  «потому что в Kotlin так можно».
 *
 *  ТРЕБОВАНИЯ
 *  1. auditLine(transfer): строка
 *     "transfer=<id> from=<from> to=<to> amount=<minor> <CUR> status=<status>".
 *  2. requireActive(account): вернуть account, если статус ACTIVE;
 *     иначе IllegalArgumentException("account is not active"). Используйте takeIf.
 *  3. describeOrNull(account): null -> null; иначе "<id>:<balance.minor>".
 *     Используйте ?.let.
 *
 *  ПОДСКАЗКИ
 *  - `transfer.let { ... }` или `with(transfer) { ... }`.
 *  - `account.takeIf { it.status == ACTIVE } ?: throw IllegalArgumentException(...)`.
 *  - `account?.let { "${it.id.value}:${it.balance.minor}" }`.
 *
 *  ЛОВУШКА
 *  Когда `also { }` уместнее `apply { }`, и почему злоупотребление
 *  scope-функциями ухудшает читаемость?
 * ═══════════════════════════════════════════════════════════
 */
fun auditLine(transfer: Transfer): String = TODO("m05.3.1")

fun requireActive(account: Account): Account = TODO("m05.3.2")

fun describeOrNull(account: Account?): String? = TODO("m05.3.3")
