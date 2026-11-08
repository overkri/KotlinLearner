package ledger.errors

import ledger.domain.Account
import ledger.domain.Money

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 6 · УПРАЖНЕНИЕ 6.2 — Result вместо исключений
 * ═══════════════════════════════════════════════════════════
 *  Тема:           kotlin.Result, runCatching, getOrElse, exceptionOrNull
 *  Java-аналогия:  Either/Result без checked exceptions
 *  Тесты:          ./gradlew :core:test -Pmodule=06 --tests "ledger.m06.ResultServiceTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте снятие денег как операцию, которая возвращает Result<Account>.
 *  Исключения допустимы только для действительно исключительных ситуаций;
 *  бизнес-отказы должны быть значениями.
 *
 *  ТРЕБОВАНИЯ
 *  1. withdraw(account, amount):
 *     - amount.minor <= 0 -> failure(IllegalArgumentException("amount must be positive"));
 *     - status != ACTIVE -> failure(IllegalStateException("account is not active"));
 *     - валюта не совпадает -> failure(IllegalArgumentException("currency mismatch"));
 *     - недостаточно средств -> failure(IllegalArgumentException("insufficient funds"));
 *     - иначе success(account.withBalance(account.balance - amount)).
 *  2. parseAndWithdraw(account, text):
 *     - parseAmount(text) из модуля 01; null -> failure(IllegalArgumentException("invalid amount"));
 *     - иначе вызвать withdraw с валютой счёта.
 *  3. Реализация должна использовать Result, а не try/catch наружу.
 *
 *  ПОДСКАЗКИ
 *  - `Result.success(value)` / `Result.failure(exception)`.
 *  - `runCatching { ... }` ловит Throwable; не используйте его для бизнес-веток,
 *     если хотите контролировать типы ошибок.
 *  - `Result.fold`, `getOrElse`, `map`, `exceptionOrNull`.
 *
 *  ЛОВУШКА
 *  Почему `runCatching` вокруг suspend-функции может поймать CancellationException
 *  и сломать отмену корутины? (Подробнее — в модуле 07.)
 * ═══════════════════════════════════════════════════════════
 */
fun withdraw(account: Account, amount: Money): Result<Account> = TODO("m06.2.1")

fun parseAndWithdraw(account: Account, text: String): Result<Account> = TODO("m06.2.2")
