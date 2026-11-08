package ledger.domain

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 3 · УПРАЖНЕНИЕ 3.2 — Value classes
 * ═══════════════════════════════════════════════════════════
 *  Тема:           @JvmInline value class, init-валидация, type safety
 *  Java-аналогия:  typed wrapper над String без лишней аллокации
 *  Тесты:          ./gradlew :core:test -Pmodule=03 --tests "ledger.m03.IdentifiersTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Замените «stringly typed» идентификаторы на value classes с валидацией.
 *
 *  ТРЕБОВАНИЯ
 *  1. AccountId и TransferId: value не пустой и не из пробелов,
 *     иначе IllegalArgumentException "id must not be blank".
 *  2. IdempotencyKey: не пустой, не из пробелов, длина <= 64, без пробельных
 *     символов. Сообщения: "key must not be blank", "key is too long",
 *     "key must not contain whitespace".
 *  3. Все три — @JvmInline value class с полем value: String.
 *  4. AccountId("a") == AccountId("a"); AccountId("a") != TransferId("a")
 *     (разные типы не сравниваются без приведения).
 *
 *  ПОДСКАЗКИ
 *  - `@JvmInline value class AccountId(val value: String)`.
 *  - В init можно использовать `any { it.isWhitespace() }`.
 *  - Value class в большинстве случаев не создаёт объект в heap.
 *
 *  ЛОВУШКА
 *  Почему value class нельзя использовать как ключ в некоторых API и когда
 *  компилятор всё же «боксит» его в объект?
 * ═══════════════════════════════════════════════════════════
 */
@JvmInline
value class AccountId(val value: String) {
    init {
        TODO("m03.2.1")
    }
}

@JvmInline
value class TransferId(val value: String) {
    init {
        TODO("m03.2.2")
    }
}

@JvmInline
value class IdempotencyKey(val value: String) {
    init {
        TODO("m03.2.3")
    }
}
