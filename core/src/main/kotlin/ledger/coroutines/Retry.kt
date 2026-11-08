package ledger.coroutines

import kotlinx.coroutines.delay

/** Временная ошибка, которую имеет смысл повторить. */
class RetryableException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 7 · УПРАЖНЕНИЕ 7.1 — Retry с экспоненциальной задержкой
 * ═══════════════════════════════════════════════════════════
 *  Тема:           suspend, delay, try/catch, повторные попытки
 *  Java-аналогия:  Spring Retry / ручной цикл с Thread.sleep
 *  Тесты:          ./gradlew :core:test -Pmodule=07 --tests "ledger.m07.RetryTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте retry с экспоненциальной задержкой. В тестах delay работает
 *  на виртуальном времени, поэтому тест не спит по-настоящему.
 *
 *  ТРЕБОВАНИЯ
 *  1. times — общее число попыток, times >= 1, иначе IllegalArgumentException
 *     "times must be at least 1".
 *  2. initialDelayMillis >= 0, иначе IllegalArgumentException
 *     "initialDelayMillis must be non-negative".
 *  3. Между попытками delay: initial, initial*2, initial*4, ...
 *  4. Повторяется только RetryableException; остальные сразу пробрасываются.
 *  5. После исчерпания попыток пробросить последний RetryableException.
 *  6. Не глотать CancellationException (структурированная конкурентность).
 *
 *  ПОДСКАЗКИ
 *  - `repeat(times) { attempt -> ... }`; `return block()` внутри try.
 *  - `catch (e: RetryableException)`; не добавляйте `catch (e: Throwable)`.
 *  - delay из kotlinx.coroutines — это suspend-функция с поддержкой отмены.
 *
 *  ЛОВУШКА
 *  Почему `Thread.sleep` внутри suspend-функции блокирует поток и ломает
 *  виртуальное время тестов? Когда нужен `withContext(Dispatchers.IO)`?
 * ═══════════════════════════════════════════════════════════
 */
suspend fun <T> retry(
    times: Int,
    initialDelayMillis: Long,
    block: suspend () -> T,
): T = TODO("m07.1.1")
