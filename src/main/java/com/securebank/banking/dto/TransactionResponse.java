package com.securebank.banking.dto;

import com.securebank.banking.enums.TransactionStatus;
import com.securebank.banking.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private String transactionReference;
    private Long accountId;
    private Long relatedAccountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;

    public TransactionResponse(
            Long id,
            String transactionReference,
            Long accountId,
            Long relatedAccountId,
            TransactionType transactionType,
            BigDecimal amount,
            BigDecimal balanceAfter,
            TransactionStatus status,
            String description,
            LocalDateTime createdAt) {

        this.id = id;
        this.transactionReference = transactionReference;
        this.accountId = accountId;
        this.relatedAccountId = relatedAccountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getRelatedAccountId() {
        return relatedAccountId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
