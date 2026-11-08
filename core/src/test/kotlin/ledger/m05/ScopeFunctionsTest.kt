package ledger.m05

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.domain.AccountStatus
import ledger.functional.auditLine
import ledger.functional.describeOrNull
import ledger.functional.requireActive
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m05")
class ScopeFunctionsTest {

    @Test
    fun `formats an audit line`() {
        auditLine(TestData.transfer()) shouldBe
            "transfer=tr-1 from=acc-1 to=acc-2 amount=1000 USD status=COMPLETED"
    }

    @Test
    fun `returns an active account`() {
        val account = TestData.account(status = AccountStatus.ACTIVE)

        requireActive(account) shouldBe account
    }

    @Test
    fun `rejects a non-active account`() {
        val frozen = TestData.account(status = AccountStatus.FROZEN)

        val error = shouldThrow<IllegalArgumentException> { requireActive(frozen) }
        error shouldHaveMessage "account is not active"
    }

    @Test
    fun `describes a nullable account`() {
        describeOrNull(null) shouldBe null
        describeOrNull(TestData.account()) shouldBe "acc-1:10000"
    }
}
