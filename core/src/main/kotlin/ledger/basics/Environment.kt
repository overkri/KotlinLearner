package ledger.basics

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 0 · УПРАЖНЕНИЕ 0.3 — Компилятор и toolchain
 * ═══════════════════════════════════════════════════════════
 *  Тема:           top-level functions, string templates, KotlinVersion
 *  Java-аналогия:  статический util-метод + BuildInfo
 *  Тесты:          ./gradlew :core:test -Pmodule=00 --tests "ledger.m00.EnvironmentTest"
 *  Уровень:        ★☆☆
 *
 *  ЗАДАНИЕ
 *  Соберите строку о среде выполнения. Это упражнение проверяет, что проект
 *  действительно собран Kotlin 2.4 на JDK 21, а не «на глаз».
 *
 *  ТРЕБОВАНИЯ
 *  1. Формат строго: "Kotlin <version> / JVM <spec-version>".
 *     Пример: "Kotlin 2.4.21 / JVM 21".
 *  2. Версия Kotlin берётся из KotlinVersion.CURRENT.
 *  3. Версия JVM берётся из системного свойства "java.specification.version".
 *
 *  ПОДСКАЗКИ
 *  - KotlinVersion.CURRENT.toString() возвращает версию компилятора.
 *  - В Kotlin нет static: top-level-функция компилируется в класс EnvironmentKt.
 *
 *  ЛОВУШКА
 *  Почему `System.getProperty(...)!!` — плохая идея, даже если свойство
 *  почти всегда существует?
 * ═══════════════════════════════════════════════════════════
 */
fun environmentSummary(): String = TODO("m00.3.1")
