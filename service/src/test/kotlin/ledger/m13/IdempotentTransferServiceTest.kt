package ledger.m13

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import ledger.domain.AccountId
import ledger.domain.IdempotencyKey
import ledger.domain.TransferError
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.http.TransferService
import ledger.messaging.CachedTransferDto
import ledger.messaging.IdempotencyStore
import ledger.messaging.IdempotentTransferService
import ledger.serialization.ledgerJson
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m13")
class IdempotentTransferServiceTest {

    private class FakeStore : IdempotencyStore {
        val values = mutableMapOf<IdempotencyKey, String>()

        override suspend fun get(key: IdempotencyKey): String? = values[key]

        override suspend fun put(key: IdempotencyKey, value: String, ttlSeconds: Long) {
            values[key] = value
        }
    }

    private class CountingDelegate(private val result: TransferResult) : TransferService {
        var calls = 0
            private set

        override suspend fun transfer(request: TransferRequest): TransferResult {
            calls += 1
            return result
        }
    }

    private fun request(key: String): TransferRequest = TransferRequest(
        from = AccountId("acc-1"),
        to = AccountId("acc-2"),
        amount = TestData.usd(100),
        idempotencyKey = IdempotencyKey(key),
    )

    @Test
    fun `returns the cached result without calling the delegate twice`() = runTest {
        val store = FakeStore()
        val delegate = CountingDelegate(TransferResult.Success(TestData.transfer(id = "tr-1")))
        val service = IdempotentTransferService(delegate, store)
        val request = request("idem-1")

        val first = service.transfer(request)
        val second = service.transfer(request)

        delegate.calls shouldBe 1
        first shouldBe second
        val cached = store.values.getValue(request.idempotencyKey)
        ledgerJson().decodeFromString<CachedTransferDto>(cached).id shouldBe "tr-1"
    }

    @Test
    fun `does not cache a business failure`() = runTest {
        val failure = TransferResult.Failure(
            TransferError.InsufficientFunds(
                accountId = AccountId("acc-1"),
                available = TestData.usd(10),
                requested = TestData.usd(100),
            ),
        )
        val store = FakeStore()
        val delegate = CountingDelegate(failure)
        val service = IdempotentTransferService(delegate, store)
        val request = request("idem-1")

        service.transfer(request) shouldBe failure
        service.transfer(request) shouldBe failure

        delegate.calls shouldBe 2
        store.values shouldBe emptyMap()
    }

    @Test
    fun `falls back to the delegate when the cache is corrupted`() = runTest {
        val store = FakeStore()
        val request = request("idem-1")
        store.values[request.idempotencyKey] = "{not-json"
        val delegate = CountingDelegate(TransferResult.Success(TestData.transfer(id = "tr-1")))
        val service = IdempotentTransferService(delegate, store)

        service.transfer(request) shouldBe delegate.transfer(request)
        delegate.calls shouldBe 2
    }

    @Test
    fun `handles different keys independently`() = runTest {
        val store = FakeStore()
        val delegate = CountingDelegate(TransferResult.Success(TestData.transfer(id = "tr-1")))
        val service = IdempotentTransferService(delegate, store)

        service.transfer(request("idem-1"))
        service.transfer(request("idem-2"))

        delegate.calls shouldBe 2
    }

    @Test
    fun `returns a failure result unchanged`() = runTest {
        val failure = TransferResult.Failure(TransferError.SameAccount)
        val store = FakeStore()
        val service = IdempotentTransferService(CountingDelegate(failure), store)

        service.transfer(request("idem-1")).shouldBeInstanceOf<TransferResult.Failure>()
    }
}
