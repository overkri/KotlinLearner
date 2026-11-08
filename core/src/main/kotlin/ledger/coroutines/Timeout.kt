package ledger.coroutines

import kotlinx.coroutines.withTimeoutOrNull

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 7 · УПРАЖНЕНИЕ 7.4 — Таймаут с fallback
 * ═══════════════════════════════════════════════════════════
 *  Тема:           withTimeoutOrNull, отмена, fallback
 *  Java-аналогия:  CompletableFuture.orTimeout + exceptionally
 *  Тесты:          ./gradlew :core:test -Pmodule=07 --tests "ledger.m07.TimeoutTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Верните fallback, если операция не успела за timeoutMillis.
 *
 *  ТРЕБОВАНИЯ
 *  1. timeoutMillis > 0, иначе IllegalArgumentException
 *     "timeoutMillis must be positive".
 *  2. Если block завершился вовремя — вернуть его результат.
 *  3. Если таймаут — вернуть fallback, не бросать TimeoutCancellationException.
 *  4. Если block бросил собственное исключение — пробросить его.
 *  5. Отмена внешней корутины должна пробрасываться.
 *
 *  ПОДСКАЗКИ
 *  - `withTimeoutOrNull(timeoutMillis) { block() } ?: fallback`.
 *  - withTimeoutOrNull отменяет block и возвращает null при таймауте.
 *  - Не используйте `runCatching` вокруг всего блока: поймаете отмену.
 *
 *  ЛОВУШКА
 *  Что произойдёт с ресурсами внутри block при таймауте? Обязательно ли
 *  использовать try/finally/use для очистки?
 * ═══════════════════════════════════════════════════════════
 */
suspend fun <T> withTimeoutFallback(
    timeoutMillis: Long,
    fallback: T,
    block: suspend () -> T,
): T = TODO("m07.4.1")
