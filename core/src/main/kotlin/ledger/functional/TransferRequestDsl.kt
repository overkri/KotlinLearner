package ledger.functional

import ledger.domain.AccountId
import ledger.domain.IdempotencyKey
import ledger.domain.Money
import ledger.domain.TransferRequest

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 5 · УПРАЖНЕНИЕ 5.2 — DSL заявки на перевод
 * ═══════════════════════════════════════════════════════════
 *  Тема:           DSL, receiver-лямбды, @DslMarker, apply
 *  Java-аналогия:  builder с method chaining
 *  Тесты:          ./gradlew :core:test -Pmodule=05 --tests "ledger.m05.TransferRequestDslTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте читаемый DSL для создания TransferRequest.
 *
 *  ТРЕБОВАНИЯ
 *  1. transferRequest { ... } создаёт заявку через builder.
 *  2. from/to/amount/idempotencyKey обязательны; null -> IllegalArgumentException
 *     с сообщением "<поле> is required" (from, to, amount, idempotencyKey).
 *  3. note опционален и по умолчанию null.
 *  4. builder помечен @TransferDsl; вложенный DSL другого типа не должен
 *     случайно вызывать методы внешнего (это проверяется компилятором).
 *  5. transferRequest не должна оставлять ссылку на builder после вызова.
 *
 *  ПОДСКАЗКИ
 *  - `TransferRequestBuilder().apply(block).build()`.
 *  - `requireNotNull(from) { "from is required" }`.
 *  - `@DslMarker annotation class TransferDsl` уже объявлена.
 *
 *  ЛОВУШКА
 *  Чем `apply` отличается от `also` и почему в DSL обычно используют apply?
 *  Что произойдёт, если забыть @DslMarker во вложенных DSL?
 * ═══════════════════════════════════════════════════════════
 */
@DslMarker
annotation class TransferDsl

@TransferDsl
class TransferRequestBuilder {
    var from: AccountId? = null
    var to: AccountId? = null
    var amount: Money? = null
    var idempotencyKey: IdempotencyKey? = null
    var note: String? = null

    fun build(): TransferRequest = TODO("m05.2.1")
}

fun transferRequest(block: TransferRequestBuilder.() -> Unit): TransferRequest = TODO("m05.2.2")
