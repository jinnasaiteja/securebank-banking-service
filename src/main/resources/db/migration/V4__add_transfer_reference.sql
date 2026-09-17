ALTER TABLE transactions
    ADD COLUMN transfer_reference VARCHAR(50) NULL
    AFTER transaction_reference;

CREATE INDEX idx_transactions_transfer_reference
    ON transactions (transfer_reference);
