package ledger.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 7 · УПРАЖНЕНИЕ 7.5 — supervisorScope
 * ═══════════════════════════════════════════════════════════
 *  Тема:           supervisorScope, async, Result, отмена
 *  Java-аналогия:  Promise.allSettled
 *  Тесты:          ./gradlew :core:test -Pmodule=07 --tests "ledger.m07.RunAllSettledTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Выполните набор задач так, чтобы падение одной не отменяло остальные,
 *  и верните Result для каждой.
 *
 *  ТРЕБОВАНИЯ
 *  1. Задачи выполняются параллельно.
 *  2. Результаты — в порядке tasks.
 *  3. Ошибка одной задачи не мешает другим завершиться.
 *  4. CancellationException родителя не должна превращаться в Result.failure:
 *     её нужно пробросить дальше.
 *  5. Другие Throwable превращаются в Result.failure.
 *
 *  ПОДСКАЗКИ
 *  - `supervisorScope { tasks.map { task -> async { ... } }.awaitAll() }`.
 *  - Внутри async: try { Result.success(task()) } catch (e: CancellationException) { throw e } catch (e: Exception) { Result.failure(e) }.
 *  - Не используйте runCatching без обработки CancellationException.
 *
 *  ЛОВУШКА
 *  Почему supervisorScope не спасает от ошибки в самом родителе? Кто всё
 *  равно отменит всех детей при исключении в теле supervisorScope?
 * ═══════════════════════════════════════════════════════════
 */
suspend fun <T> runAllSettled(tasks: List<suspend () -> T>): List<Result<T>> =
    TODO("m07.5.1")
