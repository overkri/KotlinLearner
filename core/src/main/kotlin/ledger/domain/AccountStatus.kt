package ledger.domain

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 3 · УПРАЖНЕНИЕ 3.3 — AccountStatus
 * ═══════════════════════════════════════════════════════════
 *  Тема:           enum с методами, when, переходы состояний
 *  Java-аналогия:  enum + switch + isValidTransition
 *  Тесты:          ./gradlew :core:test -Pmodule=03 --tests "ledger.m03.AccountStatusTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Опишите жизненный цикл счёта. Недопустимые переходы должны возвращать false,
 *  а не бросать исключение: это предикат, а не команда.
 *
 *  ТРЕБОВАНИЯ
 *  1. CLOSED — терминальный статус; isTerminal == true только для него.
 *  2. ACTIVE -> FROZEN, ACTIVE -> CLOSED — разрешены.
 *  3. FROZEN -> ACTIVE, FROZEN -> CLOSED — разрешены.
 *  4. CLOSED -> любой статус — запрещён.
 *  5. Переход в тот же статус возвращает false.
 *
 *  ПОДСКАЗКИ
 *  - `when (this) { ... }` внутри enum может быть исчерпывающим.
 *  - `isTerminal` — computed property без backing field.
 *  - Не путайте «предикат перехода» с самим переходом: последний реализует Account.
 *
 *  ЛОВУШКА
 *  Почему `CLOSED -> true` для «любого» перехода — потенциальная дыра в
 *  безопасности, даже если сегодня закрытый счёт нельзя активировать?
 * ═══════════════════════════════════════════════════════════
 */
enum class AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED,
    ;

    val isTerminal: Boolean
        get() = TODO("m03.3.1")

    fun canTransitionTo(target: AccountStatus): Boolean = TODO("m03.3.2")
}
