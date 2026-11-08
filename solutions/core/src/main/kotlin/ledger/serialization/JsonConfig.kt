package ledger.serialization

import kotlinx.serialization.json.Json

fun ledgerJson(): Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
    prettyPrint = false
    classDiscriminator = "type"
    isLenient = false
}
