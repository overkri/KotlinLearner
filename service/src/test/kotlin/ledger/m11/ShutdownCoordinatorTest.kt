package ledger.m11

import io.kotest.matchers.shouldBe
import ledger.http.ShutdownCoordinator
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m11")
class ShutdownCoordinatorTest {

    @Test
    fun `is ready initially`() {
        ShutdownCoordinator().isReady() shouldBe true
    }

    @Test
    fun `becomes not ready after shutdown starts`() {
        val coordinator = ShutdownCoordinator()

        coordinator.beginShutdown()

        coordinator.isReady() shouldBe false
    }

    @Test
    fun `shutdown is idempotent`() {
        val coordinator = ShutdownCoordinator()

        coordinator.beginShutdown()
        coordinator.beginShutdown()

        coordinator.isReady() shouldBe false
    }

    @Test
    fun `stays not ready`() {
        val coordinator = ShutdownCoordinator()
        coordinator.beginShutdown()

        repeat(10) {
            coordinator.isReady() shouldBe false
        }
    }
}
