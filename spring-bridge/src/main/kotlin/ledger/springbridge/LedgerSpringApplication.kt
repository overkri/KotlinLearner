package ledger.springbridge

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LedgerSpringApplication

fun main(args: Array<String>) {
    runApplication<LedgerSpringApplication>(*args)
}

@ConfigurationProperties(prefix = "ledger")
data class LedgerProperties(
    val defaultCurrency: String = "USD",
    val maxTransferMinor: Long = 1_000_000,
)
