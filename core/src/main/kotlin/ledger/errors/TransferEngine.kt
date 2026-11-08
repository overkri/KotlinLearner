package ledger.errors

import ledger.domain.Account
import ledger.domain.AccountRepository
import ledger.domain.Transfer
import ledger.domain.TransferError
import ledger.domain.TransferId
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.domain.TransferStatus
import java.time.Instant
import java.util.UUID

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 6 · УПРАЖНЕНИЕ 6.1 — Движок переводов
 * ═══════════════════════════════════════════════════════════
 *  Тема:           sealed-ошибки, early return, транзакционный сценарий
 *  Java-аналогия:  service с проверками и возвратом Result вместо исключений
 *  Тесты:          ./gradlew :core:test -Pmodule=06 --tests "ledger.m06.TransferEngineTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте перевод между двумя счетами. Ожидаемые бизнес-ошибки возвращаются
 *  как TransferResult.Failure, а не бросаются исключением.
 *
 *  ТРЕБОВАНИЯ (порядок проверок важен)
 *  1. amount.minor <= 0 -> InvalidAmount(amount).
 *  2. from == to -> SameAccount.
 *  3. Нет счёта from -> AccountNotFound(from); нет счёта to -> AccountNotFound(to).
 *  4. from.status != ACTIVE -> AccountFrozen(from); to.status != ACTIVE -> AccountFrozen(to).
 *  5. Валюта from.balance != amount.currency -> CurrencyMismatch(from.balance.currency, amount.currency);
 *     валюта to.balance != amount.currency -> CurrencyMismatch(to.balance.currency, amount.currency).
 *  6. from.balance.minor < amount.minor -> InsufficientFunds(from.id, from.balance, amount).
 *  7. Успех: списать с from, зачислить to, сохранить оба счёта, вернуть Success.
 *     TransferStatus = COMPLETED, id — из nextTransferId(), createdAt — из now().
 *  8. idempotencyKey заявки сохраняется в Transfer.
 *
 *  ПОДСКАЗКИ
 *  - `val source = accounts.findById(request.from) ?: return TransferResult.Failure(...)`.
 *  - `withBalance` уже увеличивает version.
 *  - Для успеха создайте Transfer и сохраните source.withBalance(source.balance - amount).
 *  - Используйте `-` из модуля 05 для проверки достаточности средств.
 *
 *  ЛОВУШКА
 *  Почему при падении сохранения второго счёта недостаточно «просто вернуть ошибку»?
 *  Что произойдёт с деньгами без транзакции БД? (Ответ — в модуле 12.)
 * ═══════════════════════════════════════════════════════════
 */
class TransferEngine(
    private val accounts: AccountRepository,
    private val nextTransferId: () -> TransferId = { TransferId("tr-${UUID.randomUUID()}") },
    private val now: () -> Instant = { Instant.now() },
) {
    fun transfer(request: TransferRequest): TransferResult = TODO("m06.1.1")
}
