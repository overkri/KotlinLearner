package ledger.testing

import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import io.lettuce.core.api.coroutines.RedisCoroutinesCommandsImpl
import org.testcontainers.containers.GenericContainer

object RedisFixture {

    private val container = GenericContainer("redis:7.4-alpine")
        .withExposedPorts(6379)
        .apply { start() }

    private val client = RedisClient.create(
        "redis://${container.host}:${container.getMappedPort(6379)}",
    )

    val connection: StatefulRedisConnection<String, String> = client.connect()

    val commands: RedisCoroutinesCommands<String, String> =
        RedisCoroutinesCommandsImpl(connection.reactive())

    fun flushAll() {
        connection.sync().flushall()
    }
}
