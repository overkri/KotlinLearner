package ledger.messaging

import io.lettuce.core.SetArgs
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import ledger.domain.IdempotencyKey

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 13 · УПРАЖНЕНИЕ 13.2 — Redis idempotency store
 * ═══════════════════════════════════════════════════════════
 *  Тема:           Lettuce coroutines, TTL, Redis как кэш
 *  Java-аналогия:  RedisTemplate.opsForValue().set(key, value, ttl)
 *  Тесты:          ./gradlew :service:test -Pmodule=13 --tests "ledger.m13.RedisIdempotencyStoreTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Реализуйте IdempotencyStore на Redis через Lettuce coroutines API.
 *
 *  ТРЕБОВАНИЯ
 *  1. get(key): commands.get(key.value).
 *  2. put(key, value, ttlSeconds): SET с истечением срока.
 *  3. TTL > 0, иначе IllegalArgumentException "ttlSeconds must be positive".
 *  4. Ключ — префикс "idem:" + key.value (пространство имён).
 *  5. Не блокировать поток: использовать suspend-команды.
 *
 *  ПОДСКАЗКИ
 *  - `commands.set(redisKey, value, SetArgs.Builder.ex(ttlSeconds))`.
 *  - Lettuce coroutines commands создаются из `connection.reactive()`:
 *    `RedisCoroutinesCommandsImpl(connection.reactive())`.
 *  - Redis — не база истины; TTL защищает от бесконечного роста.
 *
 *  ЛОВУШКА
 *  Что произойдёт, если TTL истёк, а клиент повторяет запрос? Почему TTL
 *  должен быть больше максимального времени жизни бизнес-операции?
 * ═══════════════════════════════════════════════════════════
 */
class RedisIdempotencyStore(
    private val commands: RedisCoroutinesCommands<String, String>,
) : IdempotencyStore {

    override suspend fun get(key: IdempotencyKey): String? = TODO("m13.2.1")

    override suspend fun put(key: IdempotencyKey, value: String, ttlSeconds: Long): Unit =
        TODO("m13.2.2")
}
