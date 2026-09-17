package com.securebank.banking.entity;

import com.securebank.banking.enums.TransactionStatus;
import com.securebank.banking.enums.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "transaction_reference",
            nullable = false,
            unique = true,
            length = 50
    )
    private String transactionReference;

    @Column(
            name = "transfer_reference",
            length = 50
    )
    private String transferReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            nullable = false
    )
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_account_id")
    private Account relatedAccount;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "transaction_type",
            nullable = false,
            length = 30
    )
    private TransactionType transactionType;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            name = "balance_after",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private TransactionStatus status;

    @Column(length = 255)
    private String description;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(String transferReference) {
        this.transferReference = transferReference;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getRelatedAccount() {
        return relatedAccount;
    }

    public void setRelatedAccount(Account relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(
            TransactionType transactionType) {

        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(
            BigDecimal balanceAfter) {

        this.balanceAfter = balanceAfter;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(
            TransactionStatus status) {

        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description) {

        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
