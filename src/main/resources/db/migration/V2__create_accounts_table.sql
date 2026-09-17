CREATE TABLE accounts (

    id BIGINT NOT NULL AUTO_INCREMENT,

    customer_id BIGINT NOT NULL,

    account_number VARCHAR(30) NOT NULL,

    account_type VARCHAR(30) NOT NULL,

    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,

    currency VARCHAR(3) NOT NULL DEFAULT 'USD',

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_accounts_account_number
        UNIQUE (account_number),

    CONSTRAINT fk_accounts_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
);
