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

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 13 · УПРАЖНЕНИЕ 13.1 — Идемпотентный сервис
 * ═══════════════════════════════════════════════════════════
 *  Тема:           idempotency key, кэш результата, JSON-снимок
 *  Java-аналогия:  Idempotency-Key header + Redis cache в Stripe
 *  Тесты:          ./gradlew :service:test -Pmodule=13 --tests "ledger.m13.IdempotentTransferServiceTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Оберните TransferService идемпотентностью: повтор с тем же ключом
 *  возвращает сохранённый результат и не вызывает delegate.
 *
 *  ТРЕБОВАНИЯ
 *  1. get(key) не null -> декодировать CachedTransferDto и вернуть Success
 *     с восстановленным Transfer.
 *  2. Иначе вызвать delegate.
 *  3. Success сохранить в store под request.idempotencyKey с ttlSeconds
 *     (JSON через ledgerJson()).
 *  4. Failure не кэшировать.
 *  5. Повреждённый кэш не должен ронять запрос: fallback к delegate.
 *  6. Разные ключи обрабатываются независимо.
 *
 *  ПОДСКАЗКИ
 *  - `json.decodeFromString<CachedTransferDto>(cached)`.
 *  - `result.transfer.toCachedDto()`.
 *  - runCatching вокруг decode, чтобы fallback при битом кэше.
 *
 *  ЛОВУШКА
 *  Между get и delegate всё ещё есть окно гонки. Как сделать атомарно?
 *  Что хранить: «в процессе» или «готовый результат»?
 * ═══════════════════════════════════════════════════════════
 */
class IdempotentTransferService(
    private val delegate: TransferService,
    private val store: IdempotencyStore,
    private val json: Json = ledgerJson(),
    private val ttlSeconds: Long = 3_600,
) : TransferService {
    override suspend fun transfer(request: TransferRequest): TransferResult = TODO("m13.1.1")
}
