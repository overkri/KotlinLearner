package ledger.m03

import io.kotest.matchers.shouldBe
import ledger.domain.AccountStatus
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m03")
class AccountStatusTest {

    @Test
    fun `only closed is terminal`() {
        AccountStatus.ACTIVE.isTerminal shouldBe false
        AccountStatus.FROZEN.isTerminal shouldBe false
        AccountStatus.CLOSED.isTerminal shouldBe true
    }

    @Test
    fun `active can freeze or close`() {
        AccountStatus.ACTIVE.canTransitionTo(AccountStatus.FROZEN) shouldBe true
        AccountStatus.ACTIVE.canTransitionTo(AccountStatus.CLOSED) shouldBe true
        AccountStatus.ACTIVE.canTransitionTo(AccountStatus.ACTIVE) shouldBe false
    }

    @Test
    fun `frozen can activate or close`() {
        AccountStatus.FROZEN.canTransitionTo(AccountStatus.ACTIVE) shouldBe true
        AccountStatus.FROZEN.canTransitionTo(AccountStatus.CLOSED) shouldBe true
        AccountStatus.FROZEN.canTransitionTo(AccountStatus.FROZEN) shouldBe false
    }

    @Test
    fun `closed cannot transition anywhere`() {
        AccountStatus.CLOSED.canTransitionTo(AccountStatus.ACTIVE) shouldBe false
        AccountStatus.CLOSED.canTransitionTo(AccountStatus.FROZEN) shouldBe false
        AccountStatus.CLOSED.canTransitionTo(AccountStatus.CLOSED) shouldBe false
    }
}
