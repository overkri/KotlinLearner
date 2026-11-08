package ledger.domain

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 3 · УПРАЖНЕНИЕ 3.4 — Account
 * ═══════════════════════════════════════════════════════════
 *  Тема:           data class с поведением, copy, инварианты, версионирование
 *  Java-аналогия:  immutable entity + with-methods + optimistic locking
 *  Тесты:          ./gradlew :core:test -Pmodule=03 --tests "ledger.m03.AccountTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Сделайте immutable-счёт с переходами статуса и изменением баланса.
 *  Каждое реальное изменение увеличивает version на 1 (optimistic locking).
 *
 *  ТРЕБОВАНИЯ
 *  1. init: ownerName не пустой ("ownerName must not be blank"),
 *     version >= 0 ("version must be non-negative").
 *  2. freeze(): ACTIVE -> FROZEN; FROZEN -> this; CLOSED -> IllegalArgumentException
 *     "closed account cannot be frozen".
 *  3. activate(): FROZEN -> ACTIVE; ACTIVE -> this; CLOSED -> IllegalArgumentException
 *     "closed account cannot be activated".
 *  4. close(): требует нулевой баланс, иначе IllegalArgumentException
 *     "account with non-zero balance cannot be closed"; CLOSED -> this.
 *  5. withBalance(newBalance): валюта должна совпадать с текущей,
 *     иначе IllegalArgumentException "currency mismatch".
 *  6. Любое реальное изменение статуса или баланса увеличивает version на 1.
 *
 *  ПОДСКАЗКИ
 *  - Используйте copy(status = ..., version = version + 1).
 *  - Для «уже в нужном статусе» возвращайте this, чтобы не плодить версии.
 *  - `require(status != CLOSED) { "..." }` — предусловие перехода.
 *
 *  ЛОВУШКА
 *  Почему изменение баланса через `account.balance = ...` невозможно и как
 *  это связано с потокобезопасностью и optimistic locking?
 * ═══════════════════════════════════════════════════════════
 */
data class Account(
    val id: AccountId,
    val ownerName: String,
    val status: AccountStatus,
    val balance: Money,
    val version: Long = 0,
) {
    init {
        TODO("m03.4.1")
    }

    fun freeze(): Account = TODO("m03.4.2")

    fun activate(): Account = TODO("m03.4.3")

    fun close(): Account = TODO("m03.4.4")

    fun withBalance(newBalance: Money): Account = TODO("m03.4.5")
}
