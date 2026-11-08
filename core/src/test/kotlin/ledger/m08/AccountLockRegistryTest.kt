package ledger.m08

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import ledger.domain.AccountId
import ledger.state.AccountLockRegistry
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m08")
class AccountLockRegistryTest {

    private val account = AccountId("acc-1")

    @Test
    fun `serializes increments for the same account`() = runTest {
        val registry = AccountLockRegistry()
        var counter = 0

        List(100) {
            launch {
                registry.withLock(account) {
                    val current = counter
                    yield()
                    counter = current + 1
                }
            }
        }.joinAll()

        counter shouldBe 100
    }

    @Test
    fun `serializes operations on the same account`() = runTest {
        val registry = AccountLockRegistry()

        val first = launch { registry.withLock(account) { delay(1_000) } }
        val second = launch { registry.withLock(account) { delay(1_000) } }
        joinAll(first, second)

        testScheduler.currentTime shouldBe 2_000L
    }

    @Test
    fun `does not serialize different accounts`() = runTest {
        val registry = AccountLockRegistry()
        val other = AccountId("acc-2")

        val first = launch { registry.withLock(account) { delay(1_000) } }
        val second = launch { registry.withLock(other) { delay(1_000) } }
        joinAll(first, second)

        testScheduler.currentTime shouldBe 1_000L
    }
}
