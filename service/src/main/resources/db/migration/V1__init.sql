CREATE TABLE accounts (
    id            VARCHAR(64) PRIMARY KEY,
    owner_name    VARCHAR(255) NOT NULL,
    status        VARCHAR(16) NOT NULL,
    balance_minor BIGINT NOT NULL CHECK (balance_minor >= 0),
    currency      VARCHAR(8) NOT NULL,
    version       BIGINT NOT NULL CHECK (version >= 0)
);

CREATE TABLE ledger_entries (
    id            BIGINT PRIMARY KEY,
    transfer_id   VARCHAR(64) NOT NULL,
    account_id    VARCHAR(64) NOT NULL,
    counterparty  VARCHAR(64) NOT NULL,
    direction     VARCHAR(16) NOT NULL,
    amount_minor  BIGINT NOT NULL CHECK (amount_minor >= 0),
    currency      VARCHAR(8) NOT NULL,
    created_at    TIMESTAMP NOT NULL
);

CREATE INDEX idx_ledger_entries_account_id ON ledger_entries (account_id);
CREATE INDEX idx_ledger_entries_transfer_id ON ledger_entries (transfer_id);
