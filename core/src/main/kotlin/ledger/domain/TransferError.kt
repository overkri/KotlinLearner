package ledger.domain

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 3 · УПРАЖНЕНИЕ 3.5 — Sealed-ошибки перевода
 * ═══════════════════════════════════════════════════════════
 *  Тема:           sealed interface, data object, исчерпывающий when
 *  Java-аналогия:  sealed interface + records + pattern matching
 *  Тесты:          ./gradlew :core:test -Pmodule=03 --tests "ledger.m03.TransferErrorTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Опишите все бизнес-ошибки перевода как sealed-иерархию. Ошибка — это
 *  значение, а не исключение: её можно вернуть, залогировать и показать клиенту.
 *
 *  ТРЕБОВАНИЯ
 *  1. AccountNotFound(accountId): "Account not found: <id>".
 *  2. AccountFrozen(accountId): "Account is frozen: <id>".
 *  3. InsufficientFunds(accountId, available, requested):
 *     "Insufficient funds: available <minor> <CUR>, requested <minor> <CUR>".
 *  4. CurrencyMismatch(from, to): "Currency mismatch: <FROM> -> <TO>".
 *  5. InvalidAmount(amount): "Invalid amount: <minor> <CUR>".
 *  6. SameAccount — data object с сообщением "Cannot transfer to the same account".
 *  7. `when (error)` по sealed-типу должен быть исчерпывающим без else.
 *
 *  ПОДСКАЗКИ
 *  - Вложенные data class внутри sealed interface дают квалифицированные имена
 *    TransferError.AccountNotFound и не конфликтуют с DomainEvent.AccountFrozen.
 *  - `data object SameAccount` — singleton с корректным equals/toString.
 *  - `${'$'}{accountId.value}` в строковом шаблоне.
 *
 *  ЛОВУШКА
 *  Что произойдёт, если добавить новую ошибку в sealed interface, а `when`
 *  в обработчике не обновить? Чем это лучше открытого class hierarchy?
 * ═══════════════════════════════════════════════════════════
 */
sealed interface TransferError {
    val message: String

    data class AccountNotFound(val accountId: AccountId) : TransferError {
        override val message: String = TODO("m03.5.1")
    }

    data class AccountFrozen(val accountId: AccountId) : TransferError {
        override val message: String = TODO("m03.5.2")
    }

    data class InsufficientFunds(
        val accountId: AccountId,
        val available: Money,
        val requested: Money,
    ) : TransferError {
        override val message: String = TODO("m03.5.3")
    }

    data class CurrencyMismatch(val from: Currency, val to: Currency) : TransferError {
        override val message: String = TODO("m03.5.4")
    }

    data class InvalidAmount(val amount: Money) : TransferError {
        override val message: String = TODO("m03.5.5")
    }

    data object SameAccount : TransferError {
        override val message: String = TODO("m03.5.6")
    }
}
