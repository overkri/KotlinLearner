package ledger.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ledger.domain.AccountId
import ledger.domain.DomainEvent

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 8 · УПРАЖНЕНИЕ 8.2 — Проекция балансов
 * ═══════════════════════════════════════════════════════════
 *  Тема:           StateFlow, update, когда-выражение по sealed-событиям
 *  Java-аналогия:  materialized view, обновляемая consumer'ом
 *  Тесты:          ./gradlew :core:test -Pmodule=08 --tests "ledger.m08.BalanceProjectorTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Поддерживайте актуальные балансы счетов по доменным событиям.
 *
 *  ТРЕБОВАНИЯ
 *  1. balances — StateFlow<Map<AccountId, Long>> с начальным значением initial.
 *  2. TransferCompleted: from -= amount.minor, to += amount.minor.
 *  3. TransferRejected и AccountFrozen не меняют балансы.
 *  4. Отсутствующий счёт считается с нулевым балансом.
 *  5. Использовать `_balances.update { ... }`, а не прямую замену value
 *     (важно для атомарности при конкурентных обновлениях).
 *
 *  ПОДСКАЗКИ
 *  - `when (event) { is DomainEvent.TransferCompleted -> ... }`.
 *  - `val current = it[event.from] ?: 0L`.
 *  - `it + (event.from to current - event.amount.minor) + (event.to to ...)`.
 *
 *  ЛОВУШКА
 *  Почему `_balances.value = _balances.value + ...` небезопасно при нескольких
 *  consumer'ах, даже если StateFlow потокобезопасен?
 * ═══════════════════════════════════════════════════════════
 */
class BalanceProjector(
    initial: Map<AccountId, Long> = emptyMap(),
) {
    private val mutableBalances = MutableStateFlow(initial)

    val balances: StateFlow<Map<AccountId, Long>> = mutableBalances.asStateFlow()

    fun apply(event: DomainEvent): Unit = TODO("m08.2.1")
}
