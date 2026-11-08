package ledger.migration

import ledger.domain.Transfer

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 14 · УПРАЖНЕНИЕ 14.5 — Review kata
 * ═══════════════════════════════════════════════════════════
 *  Тема:           code review, идемпотентность, сортировка, sealed/enum
 *  Java-аналогия:  ревью PR коллеги-джависта, который «просто перевёл» код
 *  Тесты:          ./gradlew :migration-kata:test -Pmodule=14 --tests "ledger.m14.ReviewKataTest"
 *  Уровень:        ★★★
 *
 *  В PR принесли такой код (мысленно отревьюьте его):
 *
 *      fun review(ts: List<Transfer>): List<Transfer> {
 *          val result = ArrayList<Transfer>()
 *          for (t in ts) {
 *              if (t.status == TransferStatus.COMPLETED) {
 *                  if (!result.any { it.idempotencyKey == t.idempotencyKey }) {
 *                      result.add(t)
 *                  }
 *              }
 *          }
 *          return result
 *      }
 *
 *  Проблемы: O(n²), сохраняется первое, а не последнее событие, нет
 *  сортировки, `var`/Java-стиль. Реализуйте корректную версию.
 *
 *  ТРЕБОВАНИЯ
 *  1. Оставить только COMPLETED.
 *  2. Дедуплицировать по idempotencyKey; при дублях оставить перевод
 *     с более поздним createdAt (при равенстве — с большим id.value).
 *  3. Отсортировать результат по createdAt, затем по id.value.
 *  4. Не использовать `!!`, `var`, явные циклы с `ArrayList`.
 *  5. Сложность не хуже O(n log n).
 *
 *  ПОДСКАЗКИ
 *  - `filter`, `groupBy`, `maxWithOrNull(compareBy(...))`, `sortedWith`.
 *  - Для выбора последнего: `maxWithOrNull(compareBy<Transfer> { it.createdAt }.thenBy { it.id.value })`.
 *
 *  ЛОВУШКА
 *  Почему «оставить первый» — опасная ошибка для идемпотентности? Какой
 *  статус у более позднего события может быть важнее?
 * ═══════════════════════════════════════════════════════════
 */
fun reviewTransfers(transfers: List<Transfer>): List<Transfer> = TODO("m14.5.1")
