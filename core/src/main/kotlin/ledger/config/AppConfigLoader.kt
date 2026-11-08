package ledger.config

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 9 · УПРАЖНЕНИЕ 9.4 — Конфиг из environment
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Map<String, String>, defaults, require, вложенные конфиги
 *  Java-аналогия:  @ConfigurationProperties + @Value с дефолтами
 *  Тесты:          ./gradlew :core:test -Pmodule=09 --tests "ledger.m09.AppConfigTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Соберите AppConfig из переменных окружения. Ошибки конфигурации должны
 *  падать при старте, а не через час в проде.
 *
 *  ТРЕБОВАНИЯ
 *  1. HTTP_HOST default "0.0.0.0"; HTTP_PORT default 8080, диапазон 1..65535,
 *     иначе IllegalArgumentException "HTTP_PORT must be a valid port".
 *  2. DB_URL обязателен ("DB_URL is required"); DB_USER default "ledger";
 *     DB_PASSWORD default "ledger"; DB_POOL_SIZE default 10, диапазон 1..100,
 *     иначе "DB_POOL_SIZE must be a valid pool size".
 *  3. JWT_ISSUER, JWT_AUDIENCE, JWT_JWKS_URL обязательны; сообщение
 *     "<KEY> is required".
 *  4. Пустая строка или строка из пробелов считается отсутствующей.
 *  5. Значения trim'ятся.
 *
 *  ПОДСКАЗКИ
 *  - Вспомогательная функция `Map<String, String>.required(key)`.
 *  - `env[key]?.trim()?.takeIf { it.isNotEmpty() }`.
 *  - `toIntOrNull()` и диапазон.
 *
 *  ЛОВУШКА
 *  Почему пароль нельзя логировать вместе с AppConfig? Что должно попасть
 *  в /actuator/env, а что — нет?
 * ═══════════════════════════════════════════════════════════
 */
fun loadConfig(env: Map<String, String>): AppConfig = TODO("m09.4.1")
