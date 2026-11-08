package ledger.serialization

import kotlinx.serialization.json.Json

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 9 · УПРАЖНЕНИЕ 9.3 — Конфигурация Json
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Json { }, ignoreUnknownKeys, explicitNulls, discriminator
 *  Java-аналогия:  ObjectMapper с FAIL_ON_UNKNOWN_PROPERTIES=false
 *  Тесты:          ./gradlew :core:test -Pmodule=09 --tests "ledger.m09.JsonConfigTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Соберите единый Json для API и событий.
 *
 *  ТРЕБОВАНИЯ
 *  1. ignoreUnknownKeys = true.
 *  2. encodeDefaults = true.
 *  3. explicitNulls = false.
 *  4. prettyPrint = false.
 *  5. classDiscriminator = "type".
 *  6. lenient = false.
 *
 *  ПОДСКАЗКИ
 *  - `Json { ignoreUnknownKeys = true; ... }`.
 *  - Настройки видны в `json.configuration`.
 *  - explicitNulls=false убирает null-поля из JSON, но не влияет на missing-поля при decode.
 *
 *  ЛОВУШКА
 *  Чем опасен `ignoreUnknownKeys = true` для контракта API? Когда лучше упасть
 *  на неизвестном поле, а когда — игнорировать?
 * ═══════════════════════════════════════════════════════════
 */
fun ledgerJson(): Json = TODO("m09.3.1")
