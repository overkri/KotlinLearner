package ledger.testing

import ledger.domain.Account
import ledger.domain.AccountId
import ledger.domain.AccountStatus
import ledger.domain.Currency
import ledger.domain.Direction
import ledger.domain.IdempotencyKey
import ledger.domain.LedgerEntry
import ledger.domain.Money
import ledger.domain.Transfer
import ledger.domain.TransferId
import ledger.domain.TransferStatus
import java.time.Instant

/**
 * Билдеры тестовых данных. Предоставляются готовыми: студент не тратит время
 * на конструирование длинных объектов в каждом тесте.
 */
object TestData {

    val fixedInstant: Instant = Instant.parse("2026-01-15T10:00:00Z")

    fun usd(minor: Long): Money = Money(minor, Currency.USD)
    fun eur(minor: Long): Money = Money(minor, Currency.EUR)
    fun gbp(minor: Long): Money = Money(minor, Currency.GBP)
    fun jpy(minor: Long): Money = Money(minor, Currency.JPY)

    fun account(
        id: String = "acc-1",
        ownerName: String = "Alice",
        status: AccountStatus = AccountStatus.ACTIVE,
        balance: Money = usd(100_00),
        version: Long = 0,
    ): Account = Account(
        id = AccountId(id),
        ownerName = ownerName,
        status = status,
        balance = balance,
        version = version,
    )

    fun entry(
        id: Long = 1,
        transferId: String = "tr-1",
        accountId: String = "acc-1",
        counterparty: String = "acc-2",
        direction: Direction = Direction.DEBIT,
        amount: Money = usd(10_00),
        createdAt: Instant = fixedInstant,
    ): LedgerEntry = LedgerEntry(
        id = id,
        transferId = TransferId(transferId),
        accountId = AccountId(accountId),
        counterparty = AccountId(counterparty),
        direction = direction,
        amount = amount,
        createdAt = createdAt,
    )

    fun transfer(
        id: String = "tr-1",
        from: String = "acc-1",
        to: String = "acc-2",
        amount: Money = usd(10_00),
        status: TransferStatus = TransferStatus.COMPLETED,
        idempotencyKey: String = "idem-1",
        createdAt: Instant = fixedInstant,
    ): Transfer = Transfer(
        id = TransferId(id),
        from = AccountId(from),
        to = AccountId(to),
        amount = amount,
        status = status,
        idempotencyKey = IdempotencyKey(idempotencyKey),
        createdAt = createdAt,
    )
}
