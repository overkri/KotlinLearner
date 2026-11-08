package ledger.serialization

import kotlinx.serialization.Serializable
import ledger.domain.Money
import ledger.domain.Transfer
import ledger.domain.TransferError
import ledger.domain.TransferRequest

@Serializable
data class TransferRequestDto(
    val from: String,
    val to: String,
    val amountMinor: Long,
    val currency: String,
    val idempotencyKey: String,
    val note: String? = null,
)

@Serializable
data class TransferResponseDto(
    val id: String,
    val from: String,
    val to: String,
    val amountMinor: Long,
    val currency: String,
    val status: String,
    val idempotencyKey: String,
)

@Serializable
data class ErrorDto(
    val code: String,
    val message: String,
)

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 9 · УПРАЖНЕНИЕ 9.2 — DTO и маппинг
 * ═══════════════════════════════════════════════════════════
 *  Тема:           @Serializable, граница домен/API, валидация DTO
 *  Java-аналогия:  Jackson DTO + MapStruct/ручной mapper
 *  Тесты:          ./gradlew :core:test -Pmodule=09 --tests "ledger.m09.TransferDtoTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте маппинг между JSON-контрактами и доменом. DTO не должны
 *  протекать внутрь домена, а домен — наружу.
 *
 *  ТРЕБОВАНИЯ
 *  1. TransferRequestDto.toDomain():
 *     - currency ищется по точному имени Currency (USD/EUR/GBP/JPY);
 *       неизвестная -> Result.failure(IllegalArgumentException("invalid currency: <code>"));
 *     - from/to/idempotencyKey проходят валидацию value classes;
 *     - amountMinor конструируется как Money (отрицательный -> failure);
 *     - note сохраняется.
 *  2. Transfer.toDto(): status — name; amount — minor + currency.name.
 *  3. TransferError.toErrorDto(): code по типу ошибки, message — error.message.
 *     Коды: account_not_found, account_frozen, insufficient_funds,
 *     currency_mismatch, invalid_amount, same_account.
 *  4. Не использовать !!.
 *
 *  ПОДСКАЗКИ
 *  - `runCatching { ... }` на границе API — нормально, если вы контролируете типы ошибок.
 *  - `Currency.entries.firstOrNull { it.name == currency }`.
 *  - Для code используйте `when (error)` по sealed-типу.
 *
 *  ЛОВУШКА
 *  Почему DTO с `Long amountMinor` и `String currency` удобнее, чем DTO с Money?
 *  Где должна жить валидация — в DTO или в домене?
 * ═══════════════════════════════════════════════════════════
 */
fun TransferRequestDto.toDomain(): Result<TransferRequest> = TODO("m09.2.1")

fun Transfer.toDto(): TransferResponseDto = TODO("m09.2.2")

fun TransferError.toErrorDto(): ErrorDto = TODO("m09.2.3")
