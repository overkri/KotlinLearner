package ledger.nullsafety

import ledger.legacy.LegacyCustomerDirectory

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 2 · УПРАЖНЕНИЕ 2.2 — Ленивый индекс
 * ═══════════════════════════════════════════════════════════
 *  Тема:           by lazy, инициализация по требованию, счётчик построений
 *  Java-аналогия:  double-checked locking / Supplier + volatile
 *  Тесты:          ./gradlew :core:test -Pmodule=02 --tests "ledger.m02.LazyCustomerIndexTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Постройте индекс клиентов один раз — при первом обращении, а не в конструкторе.
 *
 *  ТРЕБОВАНИЯ
 *  1. `all` — List<Customer>, строится лениво из legacy.all().values.
 *  2. Каждое построение увеличивает indexBuilds на 1.
 *  3. Повторные обращения к all/find не перестраивают индекс.
 *  4. find(id) ищет по id и возвращает null, если клиента нет.
 *  5. В индексе ровно столько элементов, сколько записей в legacy.all().
 *
 *  ПОДСКАЗКИ
 *  - `val all: List<Customer> by lazy { ... }`.
 *  - Внутри lazy можно использовать buildList { for (...) add(...) }.
 *  - `firstOrNull { it.id == id }` возвращает null без исключения.
 *
 *  ЛОВУШКА
 *  Потокобезопасен ли `by lazy` по умолчанию и чем режим SYNCHRONIZED
 *  отличается от PUBLICATION?
 * ═══════════════════════════════════════════════════════════
 */
class LazyCustomerIndex(
    private val legacy: LegacyCustomerDirectory = LegacyCustomerDirectory(),
) {
    var indexBuilds: Int = 0
        private set

    val all: List<Customer> by lazy { TODO("m02.2.1") }

    fun find(id: String): Customer? = TODO("m02.2.2")
}
