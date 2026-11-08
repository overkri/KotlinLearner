package ledger.http

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 10 · УПРАЖНЕНИЕ 10.1 — Мини-роутер
 * ═══════════════════════════════════════════════════════════
 *  Тема:           path-параметры, сопоставление маршрутов, ручной HTTP
 *  Java-аналогия:  Servlet mapping / Spring @PathVariable
 *  Тесты:          ./gradlew :service:test -Pmodule=10 --tests "ledger.m10.MiniRouterTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте простейший роутер, чтобы увидеть, что именно делает Ktor routing.
 *
 *  ТРЕБОВАНИЯ
 *  1. register(method, pattern, handler) добавляет маршрут и возвращает this
 *     для цепочки.
 *  2. Паттерн — сегменты через "/": "/accounts/{id}", "/transfers", "/".
 *  3. route(request): первый подходящий маршрут; сравнение метода без учёта
 *     регистра; параметр {name} матчит ровно один непустой сегмент.
 *  4. Лишние/недостающие сегменты — не совпадение.
 *  5. Вернуть RouteMatch(handler, params) или null.
 *
 *  ПОДСКАЗКИ
 *  - Разбейте pattern и path на сегменты: trim('/').split('/').
 *  - Для каждого сегмента: если `{name}` — сохранить параметр, иначе сравнить.
 *  - Пустой path "/" — особый случай (список сегментов пуст).
 *
 *  ЛОВУШКА
 *  Почему порядок маршрутов важен при `/accounts/{id}` и `/accounts/me`?
 *  Как Ktor решает эту проблему и почему «первый match» может быть опасен?
 * ═══════════════════════════════════════════════════════════
 */
data class MiniRequest(
    val method: String,
    val path: String,
)

data class RouteMatch(
    val handler: String,
    val params: Map<String, String>,
)

class MiniRouter {

    fun register(method: String, pattern: String, handler: String): MiniRouter =
        TODO("m10.1.1")

    fun route(request: MiniRequest): RouteMatch? = TODO("m10.1.2")
}
