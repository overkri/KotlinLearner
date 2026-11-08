package ledger.nullsafety

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 2 · УПРАЖНЕНИЕ 2.3 — Null-safety kata
 * ═══════════════════════════════════════════════════════════
 *  Тема:           vararg, ?:, takeIf, toIntOrNull, error/checkNotNull
 *  Java-аналогия:  Optional.orElse, Integer.parseInt с fallback
 *  Тесты:          ./gradlew :core:test -Pmodule=02 --tests "ledger.m02.NullSafetyKataTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Решите четыре небольшие задачи, в каждой — своя грань nullable-типов.
 *
 *  ТРЕБОВАНИЯ
 *  1. firstNonBlank(values): первое значение, которое не null и не состоит из
 *     пробелов; если такого нет — null.
 *  2. parsePort(value): null или невалидное значение -> 8080; валидный порт —
 *     1..65535; пробелы по краям допускаются.
 *  3. requireEmail(customer): email, если он не null и не пустой; иначе
 *     IllegalStateException с сообщением "customer has no email".
 *  4. normalizeName(name): trim; null/пустая строка -> "anonymous".
 *
 *  ПОДСКАЗКИ
 *  - `isNullOrBlank()` — extension для String?.
 *  - `toIntOrNull()` не бросает NumberFormatException.
 *  - `takeIf { ... }` возвращает null, если условие ложно.
 *  - `error("...")` бросает IllegalStateException.
 *
 *  ЛОВУШКА
 *  Чем `checkNotNull(x)` отличается от `requireNotNull(x)` и какой тип
 *  исключения ожидает вызывающий код в каждом случае?
 * ═══════════════════════════════════════════════════════════
 */
fun firstNonBlank(vararg values: String?): String? = TODO("m02.3.1")

fun parsePort(value: String?): Int = TODO("m02.3.2")

fun requireEmail(customer: Customer): String = TODO("m02.3.3")

fun normalizeName(name: String?): String = TODO("m02.3.4")
