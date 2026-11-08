package ledger.m00

import io.kotest.matchers.string.shouldMatch
import ledger.basics.environmentSummary
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m00")
class EnvironmentTest {

    @Test
    fun `reports Kotlin 2_4 on JDK 21`() {
        environmentSummary() shouldMatch Regex("""^Kotlin 2\.4\.\d+ / JVM 21$""")
    }
}
