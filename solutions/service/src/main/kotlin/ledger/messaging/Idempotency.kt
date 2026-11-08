package ledger.messaging

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ledger.domain.AccountId
import ledger.domain.Currency
import ledger.domain.IdempotencyKey
import ledger.domain.Money
import ledger.domain.Transfer
import ledger.domain.TransferId
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.domain.TransferStatus
import ledger.http.TransferService
import ledger.serialization.ledgerJson
import java.time.Instant

interface IdempotencyStore {
    suspend fun get(key: IdempotencyKey): String?
    suspend fun put(key: IdempotencyKey, value: String, ttlSeconds: Long)
}

@Serializable
data class CachedTransferDto(
    val id: String,
    val from: String,
    val to: String,
    val amountMinor: Long,
    val currency: String,
    val status: String,
    val idempotencyKey: String,
    val createdAt: String,
)

fun Transfer.toCachedDto(): CachedTransferDto = CachedTransferDto(
    id = id.value,
    from = from.value,
    to = to.value,
    amountMinor = amount.minor,
    currency = amount.currency.name,
    status = status.name,
    idempotencyKey = idempotencyKey.value,
    createdAt = createdAt.toString(),
)

fun CachedTransferDto.toTransfer(): Transfer = Transfer(
    id = TransferId(id),
    from = AccountId(from),
    to = AccountId(to),
    amount = Money(amountMinor, Currency.valueOf(currency)),
    status = TransferStatus.valueOf(status),
    idempotencyKey = IdempotencyKey(idempotencyKey),
    createdAt = Instant.parse(createdAt),
)

class IdempotentTransferService(
    private val delegate: TransferService,
    private val store: IdempotencyStore,
    private val json: Json = ledgerJson(),
    private val ttlSeconds: Long = 3_600,
) : TransferService {

    override suspend fun transfer(request: TransferRequest): TransferResult {
        val cached = store.get(request.idempotencyKey)
        if (cached != null) {
            val transfer = runCatching {
                json.decodeFromString<CachedTransferDto>(cached).toTransfer()
            }.getOrNull()
            if (transfer != null) {
                return TransferResult.Success(transfer)
            }
        }

        val result = delegate.transfer(request)
        if (result is TransferResult.Success) {
            store.put(
                request.idempotencyKey,
                json.encodeToString(result.transfer.toCachedDto()),
                ttlSeconds,
            )
        }
        return result
    }
}
