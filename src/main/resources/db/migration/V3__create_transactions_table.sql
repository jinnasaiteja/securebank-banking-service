CREATE TABLE transactions (

    id BIGINT NOT NULL AUTO_INCREMENT,

    transaction_reference VARCHAR(50) NOT NULL,

    account_id BIGINT NOT NULL,

    related_account_id BIGINT NULL,

    transaction_type VARCHAR(30) NOT NULL,

    amount DECIMAL(19,2) NOT NULL,

    balance_after DECIMAL(19,2) NOT NULL,

    status VARCHAR(30) NOT NULL,

    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_transactions_reference
        UNIQUE (transaction_reference),

    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id),

    CONSTRAINT fk_transactions_related_account
        FOREIGN KEY (related_account_id)
        REFERENCES accounts(id)
);
