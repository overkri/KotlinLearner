package ledger.domain

@JvmInline
value class AccountId(val value: String) {
    init {
        require(value.isNotBlank()) { "id must not be blank" }
    }
}

@JvmInline
value class TransferId(val value: String) {
    init {
        require(value.isNotBlank()) { "id must not be blank" }
    }
}

@JvmInline
value class IdempotencyKey(val value: String) {
    init {
        require(value.isNotBlank()) { "key must not be blank" }
        require(value.length <= 64) { "key is too long" }
        require(value.none { it.isWhitespace() }) { "key must not contain whitespace" }
    }
}
