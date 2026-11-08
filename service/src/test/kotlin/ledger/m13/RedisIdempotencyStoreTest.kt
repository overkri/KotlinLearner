package ledger.m13

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import ledger.domain.IdempotencyKey
import ledger.messaging.RedisIdempotencyStore
import ledger.testing.RedisFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m13")
@Tag("integration")
class RedisIdempotencyStoreTest {

    private val store = RedisIdempotencyStore(RedisFixture.commands)

    @BeforeEach
    fun reset() {
        RedisFixture.flushAll()
    }

    @Test
    fun `returns null for a missing key`() = runTest {
        store.get(IdempotencyKey("missing")) shouldBe null
    }

    @Test
    fun `stores and reads a value`() = runTest {
        store.put(IdempotencyKey("idem-1"), """{"id":"tr-1"}""", 60)

        store.get(IdempotencyKey("idem-1")) shouldBe """{"id":"tr-1"}"""
    }

    @Test
    fun `uses the idem namespace`() = runTest {
        store.put(IdempotencyKey("idem-1"), "value", 60)

        RedisFixture.commands.get("idem:idem-1") shouldBe "value"
    }

    @Test
    fun `overwrites an existing key`() = runTest {
        store.put(IdempotencyKey("idem-1"), "first", 60)
        store.put(IdempotencyKey("idem-1"), "second", 60)

        store.get(IdempotencyKey("idem-1")) shouldBe "second"
    }

    @Test
    fun `rejects a non-positive ttl`() = runTest {
        shouldThrow<IllegalArgumentException> {
            store.put(IdempotencyKey("idem-1"), "value", 0)
        } shouldHaveMessage "ttlSeconds must be positive"
    }
}
