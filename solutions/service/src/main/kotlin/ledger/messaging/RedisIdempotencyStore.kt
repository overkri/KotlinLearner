package ledger.messaging

import io.lettuce.core.SetArgs
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import ledger.domain.IdempotencyKey

class RedisIdempotencyStore(
    private val commands: RedisCoroutinesCommands<String, String>,
) : IdempotencyStore {

    override suspend fun get(key: IdempotencyKey): String? =
        commands.get(redisKey(key))

    override suspend fun put(key: IdempotencyKey, value: String, ttlSeconds: Long) {
        require(ttlSeconds > 0) { "ttlSeconds must be positive" }
        commands.set(redisKey(key), value, SetArgs.Builder.ex(ttlSeconds))
    }

    private fun redisKey(key: IdempotencyKey): String = "idem:${key.value}"
}
