package ledger.m08

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import ledger.domain.AccountId
import ledger.domain.DomainEvent
import ledger.state.TransferEventBus
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m08")
class TransferEventBusTest {

    private val event = DomainEvent.AccountFrozen(
        accountId = AccountId("acc-1"),
        occurredAt = Instant.parse("2026-03-01T10:00:00Z"),
    )

    @Test
    fun `delivers an event to a subscriber`() = runTest {
        val bus = TransferEventBus()
        val received = async { bus.events.first() }
        testScheduler.runCurrent()

        bus.publish(event)

        received.await() shouldBe event
    }

    @Test
    fun `delivers an event to all subscribers`() = runTest {
        val bus = TransferEventBus()
        val first = async { bus.events.first() }
        val second = async { bus.events.first() }
        testScheduler.runCurrent()

        bus.publish(event)

        first.await() shouldBe event
        second.await() shouldBe event
    }

    @Test
    fun `replays the last events to a late subscriber`() = runTest {
        val bus = TransferEventBus(replay = 1)

        bus.publish(event)

        bus.events.first() shouldBe event
    }

    @Test
    fun `drops events when there are no subscribers and no replay`() = runTest {
        val bus = TransferEventBus(replay = 0)

        bus.publish(event)

        withTimeoutOrNull(10) { bus.events.first() } shouldBe null
    }

    @Test
    fun `tryPublish succeeds when there is buffer space`() = runTest {
        val bus = TransferEventBus(extraBufferCapacity = 1)

        bus.tryPublish(event) shouldBe true
    }
}
